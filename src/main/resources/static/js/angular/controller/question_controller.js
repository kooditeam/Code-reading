App.controller('QuestionController', function($scope, QuestionService) {
    
    $scope.questions = QuestionService.getQuestions().then(function(data) {
        $scope.questions = data;
    })
    
})