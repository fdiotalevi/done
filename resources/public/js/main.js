(function($) {
    
    
    var _compileTemplate = function(templateId, jsonObj) {
        var template = $(templateId).html();
        return Mustache.compile(template,  ["[[", "]]"])(jsonObj);
    },
    _showDones = function() {
        $.ajax({
            type: 'GET',
            url: '/api/dones',
            dataType: 'json',
            success: function(data) { 
                $('#content').html(_compileTemplate('#dones-template', data));
            }
        });
    }, 
    _showOrHideLoginForm = function() {
        $.ajax("/api/sessions/me")
            .done(function() { 
                $('#login').hide(); 
                $('#logout').show();
                _showDones();
            })
            .fail(function() { 
                $('#login').show(); 
                $('#logout').hide();
            });
    },
    _loginForm = function() {
        $('#login-form').on('submit', function() {
            $.ajax({
                type: 'POST',
                url: '/api/sessions',
                data: $(this).serialize(),
                success: _showOrHideLoginForm
            });
            $(this).find('input[type="password"]').val('');
            return false;
        });
        $('#logout-link').on('click', function() {
            $.ajax({
                type: 'DELETE',
                url: '/api/sessions/me',
                success: _showOrHideLoginForm
            });
            $('#content').html('');
            return false;
        });
    },
    startup = function() {
        _loginForm();
        _showOrHideLoginForm();
    };


    $(document).on('ready', startup);
 
})(jQuery);


