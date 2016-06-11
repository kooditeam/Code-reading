App.service('QuestionService', function($http, $q) {

    this.getQuestions = function() {
        var deferred = $q.defer();
        $http.get("/questions").success(function(data) {
            deferred.resolve(data);
        })
        
        return deferred.promise;
    }

});