(function($) {
    
    var _showOrHideLoginForm = function() {
        $.ajax("/api/sessions/me")
            .done(function() { $('#header').hide(); })
            .fail(function() { $('#header').show(); });
    },
    _loginForm = function() {
        $('#login-form').on('submit', function() {
            $.ajax({
                type: 'POST',
                url: '/api/sessions',
                data: $(this).serialize(),
                success: _showOrHideLoginForm
            });                
            return false;
        });
    },
    startup = function() {
        _loginForm();
        _showOrHideLoginForm();
    };


    $(document).on('ready', startup);
 
})(jQuery);


