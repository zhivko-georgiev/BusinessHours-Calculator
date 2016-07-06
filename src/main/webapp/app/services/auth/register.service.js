(function () {
    'use strict';

    angular
        .module('businessHoursCalculatorApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
