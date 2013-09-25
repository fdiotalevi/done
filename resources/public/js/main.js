(function($) {

    var _compileTemplate = function(templateId, jsonObj) {
        var template = $(templateId).html();
        return Mustache.compile(template,  ["[[", "]]"])(jsonObj);
    },
    Dones = function() {
        var _this = this;

        this.show = function() {
            $.ajax({
                type: 'GET',
                url: '/api/dones',
                dataType: 'json',
                success: function(data) { 
                    $('#content').html(_compileTemplate('#dones-template', data));
                }
            });
        };

        this.delete = function() {
            $('#content').html('');  
        };
    },
    LoginLogout = function(app) {
        var _this = this;

        this.render = function() {
            $.ajax("/api/sessions/me")
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
            $('#login-form').on('submit', function() {
                $.ajax({
                    type: 'POST',
                    url: '/api/sessions',
                    data: $(this).serialize(),
                    success: _this.render
                });
                $(this).find('input[type="password"]').val('');
                return false;
            });
            $('#logout-link').on('click', function() {
                $.ajax({
                    type: 'DELETE',
                    url: '/api/sessions/me',
                    success: _this.render
                });
                app.dones.delete();
                return false;
            });
        }
    },
    startup = function() {
        var dones = new Dones();
        var app = {dones:dones};
        var login = new LoginLogout(app);

        login.init();
        login.render();
    };


    $(document).on('ready', startup);
 
})(jQuery);


