var app = angular.module('FabLatAppPublic', [ 'ui.router', 'ngMaterial', 'ng.group', 'ngMessages' ]);

app.config(function($mdThemingProvider, $urlRouterProvider, $stateProvider) {

	$mdThemingProvider.theme('default')
	.primaryPalette('light-blue', {
		'default': '700',
		'hue-1': '300', 
		'hue-2': '500',
		'hue-3': '800' 
	})
	.accentPalette('green', {
		'default': '500',
		'hue-1': '300', 
		'hue-2': '700',
		'hue-3': '900' 
	})
	.warnPalette('deep-orange', { 
	});
	
	$mdThemingProvider.theme('grey', 'default')
    .primaryPalette('grey');
	
	// Configure a dark theme with primary foreground yellow
    $mdThemingProvider.theme('docs-dark', 'default')
      .primaryPalette('yellow')
      .dark();
    
    
    // Routes config
    
    $urlRouterProvider.otherwise(function () {
        return '/';
    });
    
    $stateProvider.state({
        name: 'signup1',
        url: '/',
        templateUrl: 'signup.html',
        controller: function ($rootScope) {
        	$rootScope.model = {
                title: 'Sign Up'
            };
        }
    });
    
    $stateProvider.state({
        name: 'signup2',
        url: '/signup2',
        templateUrl: 'signup-fabs.html',
        controller: function ($rootScope) {
        	$rootScope.model = {
                title: 'Sign Up'
            };
        }
    });
    
    $stateProvider.state({
        name: 'signup-successful',
        url: '/signup-successful',
        templateUrl: 'signup-successful.html',
        controller: function ($rootScope) {
        	$rootScope.model = {
                title: 'Sign Up'
            };
        }
    });

});

// General controller, runs on top of everything
app.controller('AppCtrl', ['$rootScope', '$http', '$location', '$window', '$scope', '$mdBottomSheet', '$mdDialog', '$timeout', function($rootScope, $http, $location, $window, $scope, $mdBottomSheet, $mdDialog, $timeout) { 

	  // Bottomsheet & Modal Dialogs
	  $scope.alert = '';
	  $scope.showListBottomSheet = function($event) {
	    $scope.alert = '';
	    $mdBottomSheet.show({
	      template: '<md-bottom-sheet class="md-list md-has-header"><md-list><md-list-item class="md-2-line" ng-repeat="item in items" role="link" md-ink-ripple><md-icon md-svg-icon="{{item.icon}}" aria-label="{{item.name}}"></md-icon><div class="md-list-item-text"><h3>{{item.name}}</h3></div></md-list-item> </md-list></md-bottom-sheet>',
	      controller: 'ListBottomSheetCtrl',
	      targetEvent: $event
	    }).then(function(clickedItem) {
	      $scope.alert = clickedItem.name + ' clicked!';
	    });
	  };
	  
	  $scope.showAdd = function(ev) {
	    $mdDialog.show({
	      controller: DialogController,
	      template: '<md-dialog aria-label="Form"> <md-content class="md-padding"> <form name="userForm"> <div layout layout-sm="column"> <md-input-container flex> <label>First Name</label> <input ng-model="user.firstName"> </md-input-container> <md-input-container flex> <label>Last Name</label> <input ng-model="user.lastName"> </md-input-container> </div> <md-input-container flex> <label>Message</label> <textarea ng-model="user.biography" columns="1" md-maxlength="150"></textarea> </md-input-container> </form> </md-content> <div class="md-actions" layout="row"> <span flex></span> <md-button ng-click="answer(\'not useful\')"> Cancel </md-button> <md-button ng-click="answer(\'useful\')" class="md-primary"> Save </md-button> </div></md-dialog>',
	      targetEvent: ev,
	    })
	    .then(function(answer) {
	      $scope.alert = 'You said the information was "' + answer + '".';
	    }, function() {
	      $scope.alert = 'You cancelled the dialog.';
	    });
	  };

}]);

// Controller for signup view
app.controller('SignUp1Ctrl', function($rootScope, $scope, $http, $filter, $location) {	
	
	$rootScope.user = {};
	
	$scope.submit = function() {
		// TODO: validate input fields
		
		$rootScope.user.email = $scope.user.email;
		$rootScope.user.password = $scope.user.password;
		$rootScope.user.firstName = $scope.user.firstName;
		$rootScope.user.lastName = $scope.user.lastName;
		$rootScope.user.isFabAcademyGrad = $scope.user.isFabAcademyGrad;
		$rootScope.user.fabAcademyGradYear = $scope.user.fabAcademyGradYear;
		
		// redirect
		$location.path("/signup2");
    }
	
});


// Controller for signup-fabs view
app.controller('SignUp2Ctrl', function($rootScope, $scope, $http, $filter, $mdDialog, $location) {
	
	$scope.loading = true;
	
	// Displaying the list of labs in three columns
	$http.get('https://api.fablabs.io/v0/labs.json').then(function(response) {
		
		 $scope.labs = $filter('orderBy')(response.data.labs, 'country_code', false);
		
		// split array for showing in 3 columns
		if ($scope.labs.length % 3 == 0) {
			$scope.labs1 = $scope.labs.slice(0, $scope.labs.length / 3);
			$scope.labs2 = $scope.labs.slice($scope.labs.length / 3, 2 * $scope.labs.length / 3);
			$scope.labs3 = $scope.labs.slice(2 * $scope.labs.length / 3, $scope.labs.length);
			
		} else if (($scope.labs.length + 1) % 3 == 0) {
			// could be 11, 14, 17, 20...
			// add to the first and to the second column one extra element
			$scope.labs1 = $scope.labs.slice(0, $scope.labs.length / 3 + 1);
			$scope.labs2 = $scope.labs.slice($scope.labs.length / 3, 2 * $scope.labs.length / 3 + 1);
			$scope.labs3 = $scope.labs.slice(2 * $scope.labs.length / 3, $scope.labs.length);
			
		} else {
			// could be 10, 13, 16...
			// add to the first column one extra element
			$scope.labs1 = $scope.labs.slice(0, $scope.labs.length / 3 + 1);
			$scope.labs2 = $scope.labs.slice($scope.labs.length / 3, 2 * $scope.labs.length / 3);
			$scope.labs3 = $scope.labs.slice(2 * $scope.labs.length / 3, $scope.labs.length);
		}
		
	}).finally(function() {
	    // called no matter success or failure
	    $scope.loading = false;
	});
	
	$scope.showConfirm = function(event, idLab, labName) {
	    var confirm = $mdDialog.confirm()
	          .title("Join '" + labName + "'")
	          .textContent('A request for joining this lab will be send to the Lab Manager. Would you like to continue?')
	          .ariaLabel('Confirm')
	          .targetEvent(event)
	          .ok('Continue')
	          .cancel('Cancel');
	    
	    $mdDialog.show(confirm).then(function() {
	      $scope.status = 'Fab selected.';
	      $rootScope.user.idLab = idLab;
	      
	      console.log($rootScope.user.email + ", " + $rootScope.user.password + ", " + $rootScope.user.firstName + ", " + $rootScope.user.lastName + ", " + $rootScope.user.isFabAcademyGrad + ", " + $rootScope.user.fabAcademyGradYear + ", " + $rootScope.user.idLab);
	     
	      $http.post('/resource/public/signup', {
				// idFabber: $rootScope.user.id,
	    	  	username: $rootScope.user.email,
				email: $rootScope.user.email,
				password: $rootScope.user.password,
				firstName: $rootScope.user.firstName,
				lastName: $rootScope.user.lastName,
				isFabAcademyGrad: $rootScope.user.isFabAcademyGrad,
				fabAcademyGradYear: $rootScope.user.fabAcademyGradYear,
				idLab: $rootScope.user.idLab
			}).then(function(response) {
				$location.path("/signup-successful");
			});
	      
	    }, function() {
	      $scope.status = 'Return to list.';
	    });
	  }
	  
	/*$scope.saveNomade = function() {
		  $http.post('/resource/fabber/save-nomade', {}).then(function(response) {
				$location.path("/signup-groups");				
		  });
	  }*/
});
