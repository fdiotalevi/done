(function($) {

    var _compileTemplate = function(templateId, jsonObj) {
        var template = $(templateId).html();
        return Mustache.compile(template,  ["[[", "]]"])(jsonObj);
    },
    Urls = {
        dones: '/api/dones/',
        current_session: '/api/sessions/me',
        sessions: 'api/sessions',
        users: 'api/users'
    },
    Dones = function(app) {
        var _this = this,
            _initialContent = $('#content').html(),
            _prettifyDates = function(selector) {
                $(selector).each(function() {
                    var element = $(this);
                    var date = moment(element.html(), "YYYYMDD");
                    element.html(date.format('dddd, MMMM Do YYYY'));
                });
            };
        
        this.init = function() {
            $('#content').on('submit', '#insert-done-form', function() {
                $.ajax({
                    url: app.urls.dones,
                    type: 'POST',
                    data: $(this).serialize(),
                    success: _this.show
                });
                return false;
            });
            $('#content').on('click', '.delete', function() {
                var _confirm = confirm('Are you sure you want to delete this done?');
                var id = $(this).attr('data-delete');
                if (_confirm == true) {
                    $.ajax({
                        url: app.urls.dones + id,
                        type: 'DELETE',
                        success: _this.show
                    });
                };
                return false;
            });
        };

        this.show = function() {
            $.ajax({
                type: 'GET',
                url: app.urls.dones,
                dataType: 'json',
                success: function(data) { 
                    $('#content').html(_compileTemplate('#dones-template', data));
                    _prettifyDates('#content .date');
                }
            });
        };

        this.delete = function() {
            $('#content').html(_initialContent);  
        };
    },
    LoginLogout = function(app) {
        var _this = this,
            _ok_feedback = function() {
            $('#content').html('<div class="alert alert-success">Thanks, your account has been created. <br><br><br> You can <a href="/">login</a> now</div>');
        };

        this.render = function() {
            $.ajax(app.urls.current_session)
                .done(function() { 
                    $('#login').hide(); 
                    $('#logout').show();
                    app.dones.show();
                })
                .fail(function() { 
                    $('#login').show(); 
                    $('#logout').hide();
                });
        },
        this.init = function() {
            $('#register-form').on('submit', function() {
                $.ajax({
                    type: 'POST',
                    url: app.urls.users,
                    data: $(this).serialize(),
                    success: _ok_feedback
                });
                return false;
            });
            $('#login-form').on('submit', function(event) {
                $.ajax({
                    type: 'POST',
                    url: app.urls.sessions,
                    data: $(this).serialize(),
                    success: _this.render
                });
                $(this).find('input[type="password"]').val('');
                return false;
            });
            $('#logout-link').on('click', function() {
                $.ajax({
                    type: 'DELETE',
                    url: app.urls.current_session,
                    success: _this.render
                });
                app.dones.delete();
                return false;
            });
        }
    },
    startup = function() {
        var dones = new Dones({urls: Urls});
        dones.init();

        var login = new LoginLogout({dones:dones, urls:Urls});
        login.init();
        login.render();
    };


    $(document).on('ready', startup);
 
})(jQuery);


