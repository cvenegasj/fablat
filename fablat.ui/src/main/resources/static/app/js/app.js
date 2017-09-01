var app = angular.module('FabLatApp', [ 'ui.router', 'ngMaterial', 'ngMessages', 'angularMoment', 'vsGoogleAutocomplete' ]);

app.config(function($mdThemingProvider, $mdIconProvider, $urlRouterProvider, $stateProvider) {

	$mdThemingProvider.theme('default')
		.primaryPalette('cyan', {
			'default': '500',
			'hue-1': '300', 
			'hue-2': '700',
			'hue-3': '800' 
		})
		.accentPalette('light-blue', {
			'default': '500',
			'hue-1': '300', 
			'hue-2': '700',
			'hue-3': '900' 
		})
		.warnPalette('deep-orange', { 
			
		});
	
	// Neutral theme 
	$mdThemingProvider.theme('neutral').primaryPalette('grey', {
		'default': '500',
		'hue-1': '50', 
		'hue-2': '100',
		'hue-3': '200' 
	});
	
	// Dark palettes
	$mdThemingProvider.theme('darkRed').backgroundPalette('red').dark();
	$mdThemingProvider.theme('darkPink').backgroundPalette('pink').dark();
	$mdThemingProvider.theme('darkPurple').backgroundPalette('purple').dark();
	$mdThemingProvider.theme('darkDeepPurple').backgroundPalette('deep-purple').dark();
	$mdThemingProvider.theme('darkIndigo').backgroundPalette('indigo').dark();
	$mdThemingProvider.theme('darkBlue').backgroundPalette('blue').dark();
	$mdThemingProvider.theme('darkLightBlue').backgroundPalette('light-blue').dark();
	$mdThemingProvider.theme('darkCyan').backgroundPalette('cyan').dark();
	$mdThemingProvider.theme('darkTeal').backgroundPalette('teal').dark();
	$mdThemingProvider.theme('darkGreen').backgroundPalette('green').dark();
	$mdThemingProvider.theme('darkLightGreen').backgroundPalette('light-green').dark();
	$mdThemingProvider.theme('darkLime').backgroundPalette('lime').dark();
	$mdThemingProvider.theme('darkYellow').backgroundPalette('yellow').dark();
	$mdThemingProvider.theme('darkAmber').backgroundPalette('amber').dark();
	$mdThemingProvider.theme('darkOrange').backgroundPalette('orange').dark();
	$mdThemingProvider.theme('darkDeepOrange').backgroundPalette('deep-orange').dark();
	$mdThemingProvider.theme('darkBrown').backgroundPalette('brown').dark();
	$mdThemingProvider.theme('darkGrey').backgroundPalette('grey').dark();
	$mdThemingProvider.theme('darkBlueGrey').backgroundPalette('blue-grey').dark();
	
	// Normal palettes
	$mdThemingProvider.theme('lime').backgroundPalette('lime');
	
	// Iconset
	$mdIconProvider.defaultIconSet('images/mdi.svg');
    
    // Routes config
    $urlRouterProvider.otherwise(function() {
        return '/';
    });

    $stateProvider.state({
        name: 'dashboard',
        abstract: true,
        url: '/',
        templateUrl: 'dashboard.html'
    });
    
    $stateProvider.state({
        name: 'dashboard.groups',
        url: '',
        templateUrl: 'dashboard.groups.html'
    });
    
    $stateProvider.state({
        name: 'dashboard.general-activity',
        url: '/general-activity',
        templateUrl: 'dashboard.general-activity.html'
    });
    
    $stateProvider.state({
        name: 'groups',
        url: '/groups',
        templateUrl: 'groups.html'
    });
    
    $stateProvider.state({
        name: 'workshops',
        url: '/workshops',
        templateUrl: 'workshops.html'
    });
    
    $stateProvider.state({
        name: 'settings',
        abstract: true,
        url: '/settings',
        templateUrl: 'settings.html',
        controller: function($scope){
            $scope.currentNavItem = 'profile';
        }
    });
    
    $stateProvider.state({
        name: 'settings.profile',
        url: '/profile',
        templateUrl: 'settings.profile.html'
    });
    
    $stateProvider.state({
        name: 'settings.password',
        url: '/password',
        templateUrl: 'settings.password.html'
    });
    
    $stateProvider.state({
        name: 'fabber',
        abstract: true,
        url: '/fabber/:idFabber',
        templateUrl: 'fabber.html',
        controller: function($scope) {
            $scope.currentNavItem = 'groups';
        }
    });
    
    $stateProvider.state({
        name: 'fabber.groups',
        url: '',
        templateUrl: 'fabber.groups.html'
    });
    
    $stateProvider.state({
        name: 'fabber.activity',
        url: '/activity',
        templateUrl: 'fabber.activity.html'
    });
    
    $stateProvider.state({
        name: 'group-out',
        url: '/group-out/:idGroup',
        templateUrl: 'group-out.html'
    });
    
    $stateProvider.state({
        name: 'subgroup-out',
        url: '/subgroup-out/:idSubgroup',
        templateUrl: 'subgroup-out.html'
    });
    
    $stateProvider.state({
        name: 'workshop-out',
        url: '/workshop-out/:idWorkshop',
        templateUrl: 'workshop-out.html'
    });
    
    
    /*========== group states ==========*/
    $stateProvider.state({
        name: 'group',
        abstract: true,
        url: '/group/:idGroup',
        templateUrl: 'group.html',
        resolve: {
        	redirectIfNotMember: function($http, $stateParams, $state, $q, $timeout) {
        		var deferred = $q.defer();
        		$http.get('/resource/auth/groups/' + $stateParams.idGroup)
	        		.then(function(response) {
	        			// if user is not member redirect to external page
	        			if (response.data.amIMember) {
	        				deferred.resolve();
	        			} else {
	        				$timeout(function () {
	    				      $state.go("group-out", { idGroup: $stateParams.idGroup }, {});
	    				    });
	        				deferred.reject();
	        			}	
	        		});
        		return deferred.promise;
        	},
        	group: function($http, $stateParams) {	
        		return $http.get('/resource/auth/groups/' + $stateParams.idGroup)
            		.then(function(response) {
            			return response.data;
            		});
        	}
        },
        controller: function($scope, group){
            $scope.group = group;
        }
    });
    
    $stateProvider.state({
        name: 'group.general',
        url: '/general',
        templateUrl: 'group.general.html',
        
    });
    
    $stateProvider.state({
        name: 'group.discussion',
        url: '/discussion',
        templateUrl: 'group.discussion.html'
    });
    
    $stateProvider.state({
        name: 'group.activity',
        url: '/activity',
        templateUrl: 'group.activity.html'
    });
    
    $stateProvider.state({
        name: 'group.management',
        abstract: true,
        url: '/management',
        templateUrl: 'group.management.html',
        controller: function($scope){
            $scope.currentNavItem = 'general';
        }
    });
    
    $stateProvider.state({
        name: 'group.management.general',
        url: '/general',
        templateUrl: 'group.management.general.html',
        controller: function($scope){
            $scope.currentNavItem = 'general';
        }
    });
    
    $stateProvider.state({
        name: 'group.management.members',
        url: '/members',
        templateUrl: 'group.management.members.html',
        controller: function($scope){
            $scope.currentNavItem = 'members';
        }
    });
    
    $stateProvider.state({
        name: 'group.management.subgroups',
        url: '/subgroups',
        templateUrl: 'group.management.subgroups.html',
        controller: function($scope){
            $scope.currentNavItem = 'subgroups';
        }
    });
    
    
    /*=========== subgroup states ===========*/
    $stateProvider.state({
        name: 'subgroup',
        abstract: true,
        url: '/subgroup/:idSubgroup',
        templateUrl: 'subgroup.html',
        resolve: {
        	redirectIfNotMember: function($http, $stateParams, $state, $q, $timeout) {
        		var deferred = $q.defer();
        		$http.get('/resource/auth/subgroups/' + $stateParams.idSubgroup)
        		.then(function(response) {
        			// if user is not member redirect to external page
        			if (response.data.amIMember) {
        				deferred.resolve();
        			} else {
        				$timeout(function () {
    				      $state.go("subgroup-out", { idSubgroup: $stateParams.idSubgroup }, {});
    				    });
        				deferred.reject();
        			}	
        		});
        		return deferred.promise;
        	},
        	subgroup: function($http, $stateParams) {	
        		return $http.get('/resource/auth/subgroups/' + $stateParams.idSubgroup)
            		.then(function(response) {
            			return response.data;
            		});
        	}
        },
        controller: function($scope, subgroup){
            $scope.subgroup = subgroup;
        }
    });
    
    $stateProvider.state({
        name: 'subgroup.general',
        url: '/general',
        templateUrl: 'subgroup.general.html'
    });
    
    $stateProvider.state({
        name: 'subgroup.discussion',
        url: '/discussion',
        templateUrl: 'subgroup.discussion.html'
    });
    
    $stateProvider.state({
        name: 'subgroup.ideas',
        url: '/ideas',
        templateUrl: 'subgroup.ideas.html'
    });
    
    $stateProvider.state({
        name: 'subgroup.activity',
        url: '/activity',
        templateUrl: 'subgroup.activity.html'
    });
    
    $stateProvider.state({
        name: 'subgroup.management',
        abstract: true,
        url: '/management',
        templateUrl: 'subgroup.management.html',
        controller: function($scope){
            $scope.currentNavItem = 'general';
        }
    });
    
    $stateProvider.state({
        name: 'subgroup.management.general',
        url: '/general',
        templateUrl: 'subgroup.management.general.html',
        controller: function($scope){
            $scope.currentNavItem = 'general';
        }
    });
    
    $stateProvider.state({
        name: 'subgroup.management.members',
        url: '/members',
        templateUrl: 'subgroup.management.members.html',
        controller: function($scope){
            $scope.currentNavItem = 'members';
        }
    });
    
    $stateProvider.state({
        name: 'subgroup.management.workshops',
        url: '/workshops',
        templateUrl: 'subgroup.management.workshops.html',
        controller: function($scope){
            $scope.currentNavItem = 'workshops';
        }
    });
    
    $stateProvider.state({
        name: 'subgroup.addWorkshop',
        url: '/addWorkshop',
        templateUrl: 'subgroup.add-workshop.html'
    });
    
    
    /*=========== workshop states ===========*/
    
    $stateProvider.state({
        name: 'workshop',
        abstract: true,
        url: '/workshop/:idWorkshop',
        templateUrl: 'workshop.html',
        resolve: {
        	redirectIfNotTutor: function($http, $stateParams, $state, $q, $timeout) {
        		var deferred = $q.defer();
        		$http.get('/resource/auth/workshops/' + $stateParams.idWorkshop)
        		.then(function(response) {
        			// if user is not tutor redirect to external page
        			if (response.data.amITutor) {
        				deferred.resolve();
        			} else {
        				$timeout(function () {
    				      $state.go("workshop-out", { idWorkshop: $stateParams.idWorkshop }, {});
    				    });
        				deferred.reject();
        			}	
        		});
        		return deferred.promise;
        	}
        }
    });
    
    $stateProvider.state({
        name: 'workshop.general',
        url: '/general',
        templateUrl: 'workshop.general.html'
    });
    
    $stateProvider.state({
        name: 'workshop.management',
        abstract: true,
        url: '/management',
        templateUrl: 'workshop.management.html',
        controller: function($scope){
            $scope.currentNavItem = 'general';
        }
    });
    
    $stateProvider.state({
        name: 'workshop.management.general',
        url: '/general',
        templateUrl: 'workshop.management.general.html'
    });
    
    $stateProvider.state({
        name: 'workshop.management.tutors',
        url: '/tutors',
        templateUrl: 'workshop.management.tutors.html'
    });
    
});

// General controller, runs on top of everything
app.controller('AppCtrl', ['$rootScope', '$http', '$state', '$location', '$window', '$scope', '$mdSidenav', '$mdDialog', function($rootScope, $http, $state, $location, $window, $scope, $mdSidenav, $mdDialog) { 
	
	$rootScope.user = {};
	$rootScope.isLoading = false;
	
	$http.get('/user').then(function(response) {
		if (response.data) {
			$rootScope.authenticated = true;
			$rootScope.user.id = response.data.principal.id;
			$rootScope.user.email = response.data.principal.email;
			$rootScope.user.firstName = response.data.principal.firstName;
			$rootScope.user.lastName = response.data.principal.lastName;
			$rootScope.user.idLab = response.data.principal.idLab;
			// roles
			$rootScope.user.hasAdminGeneralRole = response.data.principal.authorities.find(x => x.authority === 'ROLE_ADMIN_GENERAL') ? true : false;
			$rootScope.user.hasAdminLabRole = response.data.principal.authorities.find(x => x.authority === 'ROLE_ADMIN_LAB') ? true : false;
			
			// redirect
			/*$http.get('/resource/first', {
				params: {
					idFabber: $rootScope.user.id
				}
			}).then(function(response) {
				var firstTime = response.data.firstTime;
				
				if (firstTime === "TRUE") {
					// redirect for completing user data
					$rootScope.hiddenSidenav = true;
					$location.path("/signup-user");
				}
			});*/	
			
		} else {
			$rootScope.authenticated = false;
			console.log("not authenticated");
			// redirect to login page
			//$window.location.href = '/login';
		}
	}, function() {
		$rootScope.authenticated = false;
		console.log("error in authentication.");
		//$window.location.href = '/login'; // redirect to login page
	});

	$scope.logout = function() {
		$http.post('/login?logout', {}).finally(function() {
			$rootScope.authenticated = false;
			$rootScope.user = {};
			$window.location.href = '/';
			console.log("Logout successful.");
		});
	} 
	
	// Toolbar search toggle
	$scope.toggleSearch = function(element) {
	    $scope.showSearch = !$scope.showSearch;
	}; 

	// Sidenav toggle
	$scope.toggleSidenav = function(menuId) {
		$mdSidenav(menuId).toggle();
	};

}]);


/*========== General controllers ==========*/

// Controller in: dashboard.html
app.controller('DashboardCtrl', function($rootScope, $scope, $http) {
	
	$rootScope.isLoading = true;
	$scope.loading1 = true;
	
	$scope.currentNavItem = 'groups';
	
	$http.get('/resource/auth/fabbers/me/general')
		.then(function(response) {
			$scope.fabber = response.data;
		}).finally(function() {
		    // called no matter success or failure
		    $scope.loading1 = false;
		    $rootScope.isLoading = $scope.loading1;
		});
	
});

// Controller in: dashboard.groups.html
app.controller('DashboardGroupsCtrl', function($rootScope, $scope, $http, $mdDialog) {
	
	$scope.noGroups = false;
	$scope.groups1 = [];
	$scope.groups2 = [];
	$scope.groups3 = [];
	
	$http.get('/resource/auth/groups/find-all-mine')
		.then(function(response) {
			if (response.data.length === 0) {
				$scope.noGroups = true;
			}
			// for displaying data in 3 columns
			for (i = 0; i < response.data.length; i++) {
				if (i % 3 == 0) {
					$scope.groups1.push(response.data[i]);
				} else if ((i + 2) % 3 == 0) {
					$scope.groups2.push(response.data[i]);
				} else {
					$scope.groups3.push(response.data[i]);
				}
			}
		}).finally(function() {
		    // called no matter success or failure
		    $scope.loading = false;
		});
	
	// New group dialog
	$scope.addGroup = function(ev) {
	    $mdDialog.show({
	      controller: AddGroupDialogController,
	      templateUrl: 'add-group-dialog.tmpl.html',
	      parent: angular.element(document.body),
	      targetEvent: ev,
	      clickOutsideToClose: true,
	      fullscreen: true // Only for -xs, -sm breakpoints.
	    })
	    .then(function(answer) {
	      // ok
	    }, function() {
	      // cancel
	    });
	};
	
});

// Controller in: dashboard.general-activity.html
app.controller('DashboardGeneralActivityCtrl', function($rootScope, $scope, $http, moment) {
	
	$http.get('/resource/auth/activities/find-all-external')
		.then(function(response) {
			$scope.activities = response.data;
		}).finally(function() {
			
		});
	
});

// Controller in: groups.html
app.controller('GroupsCtrl', function($rootScope, $scope, $http, $state, $mdDialog) {
	
	$rootScope.isLoading = true;
	$scope.loading1 = true;
	$scope.groups1 = [];
	$scope.groups2 = [];
	$scope.groups3 = [];
	
	$scope.getMatches = function(searchText) {
	    return $http
	      .get("/resource/auth/groups/search/" + searchText)
	      .then(function(response) {
	    	  // Map the response object to the data object.
	    	  return response.data;
	      });
	};
	
	$http.get('/resource/auth/groups')
		.then(function(response) {
			// for displaying data in 3 columns
			for (i = 0; i < response.data.length; i++) {
				if (i % 3 == 0) {
					$scope.groups1.push(response.data[i]);
				} else if ((i + 2) % 3 == 0) {
					$scope.groups2.push(response.data[i]);
				} else {
					$scope.groups3.push(response.data[i]);
				}
			}
		}).finally(function() {
		    // called no matter success or failure
		    $scope.loading1 = false;
		    $rootScope.isLoading = $scope.loading1;
		});
	
	$scope.joinGroup = function(idGroup) {
		$http.post('/resource/auth/groups/' + idGroup + "/join")
		.then(function(response) {
			$state.go("group.general", { idGroup: idGroup }, {});
		});
	}
	
	// New group dialog
	$scope.addGroup = function(ev) {
	    $mdDialog.show({
	      controller: AddGroupDialogController,
	      templateUrl: 'add-group-dialog.tmpl.html',
	      parent: angular.element(document.body),
	      targetEvent: ev,
	      clickOutsideToClose: true,
	      fullscreen: true // Only for -xs, -sm breakpoints.
	    })
	    .then(function(answer) {
	      // ok
	    }, function() {
	      // cancel
	    });
	};
	
});

// Controller in: workshops.html
app.controller('WorkshopsCtrl', function($rootScope, $scope, $http) {
	
	$rootScope.isLoading = true;
	$scope.loading1 = true;
	
	$http.get('/resource/auth/workshops/upcoming')
		.then(function(response) {
			$scope.workshops = response.data;
		}).finally(function() {
		    // called no matter success or failure
		    $scope.loading1 = false;
		    $rootScope.isLoading = $scope.loading1;
		});
	
});

// Controller in: group-out.html
app.controller('GroupOutCtrl', function($scope, $http, $state, $stateParams) {
	
	// Injects the group object in the parent scope
	$http.get('/resource/auth/groups/' + $stateParams.idGroup)
		.then(function(response) {
			$scope.group = response.data;			
		}).finally(function() {
		    // called no matter success or failure
		});
	
	$scope.joinGroup = function(idGroup) {
		$http.post('/resource/auth/groups/' + idGroup + "/join")
			.then(function(response) {
				$state.go("group.general", { idGroup: idGroup }, {});
			});
	}
	
});

// Controller in: subgroup-out.html
app.controller('SubgroupOutCtrl', function($scope, $http, $state, $stateParams) {
	
	// Injects the subgroup object in the parent scope
	$http.get('/resource/auth/subgroups/' + $stateParams.idSubgroup)
		.then(function(response) {
			$scope.subgroup = response.data;			
		}).finally(function() {
		    // called no matter success or failure
		});
	
	$scope.joinSubgroup = function(idSubGroup) {
		$http.post('/resource/auth/subgroups/' + idSubGroup + "/join")
			.then(function(response) {
				$state.go("subgroup.general", { idSubgroup: idSubGroup }, {});
			});
	}
	
});

// Controller in: workshop-out.html
app.controller('WorkshopOutCtrl', function($scope, $http, $stateParams) {
	
	$http.get('/resource/auth/workshops/' + $stateParams.idWorkshop)
		.then(function(response) {
			$scope.workshop = response.data;			
		}).finally(function() {
		    // called no matter success or failure
		});
	
});


/*========== Settings controllers ==========*/

// Controller in: settings.profile.html
app.controller('SettingsProfileCtrl', function($scope, $http, $stateParams, $state, $mdToast) {	
	var self = this;
	
	$scope.newLab = false;
	
	$http.get('/resource/auth/fabbers/me/profile')
		.then(function(response) {
			$scope.fabber = response.data;	
		}).finally(function() {
		    // called no matter success or failure
		});
	
	
	// autocomplete for labs
	$scope.getMatches = function(searchText) {
	    return $http
	      .get("/resource/auth/labs/search/" + searchText)
	      .then(function(response) {
	    	  // Map the response object to the data object.
	    	  return response.data;
	      });
	};
	
	$scope.submit = function() {
		// submit data
		$http.put('/resource/auth/fabbers/me/update', {
			firstName: $scope.fabber.firstName,
			lastName: $scope.fabber.lastName,
			isFabAcademyGrad: $scope.fabber.isFabAcademyGrad,
			fabAcademyGradYear: $scope.fabber.isFabAcademyGrad ? $scope.fabber.fabAcademyGradYear : null,
			city: $scope.fabber.city,
			country: $scope.fabber.country,
			mainQuote: $scope.fabber.mainQuote,
			weekGoal: $scope.fabber.weekGoal,
			labId: $scope.newLab ? self.selectedLab.idLab : $scope.fabber.labId
		}).then(function successCallback(response) {	
			console.log("saved!");
			$mdToast.show(
		      $mdToast.simple()
		        .textContent('Info updated!')
		        .position("bottom right")
		        .hideDelay(1500)
			);
			// reload current state
			$state.go($state.current, {}, {reload: true});	
		}, function errorCallback(response) {
			$mdToast.show(
		      $mdToast.simple()
		        .textContent('Something went wrong :(')
		        .position("bottom right")
		        .hideDelay(1500)
			);
		});
	};
	
});

// Controller in: settings.password.html
app.controller('SettingsPasswordCtrl', function($scope, $http, $stateParams, $state, $mdToast) {	
	
	$scope.submit = function() {
		// submit data
		$http.post('/resource/auth/fabbers/me/update-password', {
			password: $scope.password,
			newPassword: $scope.newPassword,
			passwordConfirmation: $scope.passwordConfirmation
		}).then(function successCallback(response) {	
			console.log("saved!");
			$mdToast.show(
		      $mdToast.simple()
		        .textContent('Password updated!')
		        .position("bottom right")
		        .hideDelay(1500)
			);
			// reload current state
			$state.go($state.current, {}, {reload: true});	
		}, function errorCallback(response) {
			$mdToast.show(
		      $mdToast.simple()
		        .textContent("Couldn't update your password :(")
		        .position("bottom right")
		        .hideDelay(1500)
			);
		});
	};
	
});


/*========== Fabber controllers ==========*/

// Controller in: fabber.html
app.controller('FabberCtrl', function($scope, $http, $stateParams) {	
	
	// Injects the fabber object in the parent scope
	$http.get('/resource/auth/fabbers/' + $stateParams.idFabber)
		.then(function(response) {
			$scope.fabberLocal = response.data;	
		}).finally(function() {
		    // called no matter success or failure
		});
		
});

// Controller in: fabber.groups.html
app.controller('FabberGroupsCtrl', function($scope, $http, $stateParams, $state) {	
	
	$scope.groups1 = [];
	$scope.groups2 = [];
	$scope.groups3 = [];
	
	$http.get('/resource/auth/groups/find-all-fabber/' + $stateParams.idFabber)
		.then(function(response) {
			if (response.data.length === 0) {
				$scope.noGroups = true;
			}
			// for displaying data in 3 columns
			for (i = 0; i < response.data.length; i++) {
				if (i % 3 == 0) {
					$scope.groups1.push(response.data[i]);
				} else if ((i + 2) % 3 == 0) {
					$scope.groups2.push(response.data[i]);
				} else {
					$scope.groups3.push(response.data[i]);
				}
			}
		}).finally(function() {
		    // called no matter success or failure
		    $scope.loading = false;
		});
	
	// New group dialog
	$scope.addGroup = function(ev) {
	    $mdDialog.show({
	      controller: AddGroupDialogController,
	      templateUrl: 'add-group-dialog.tmpl.html',
	      parent: angular.element(document.body),
	      targetEvent: ev,
	      clickOutsideToClose: true,
	      fullscreen: true // Only for -xs, -sm breakpoints.
	    })
	    .then(function(answer) {
	      // ok
	    }, function() {
	      // cancel
	    });
	};
	
});

// Controller in: fabber.activity.html
app.controller('FabberActivityCtrl', function($scope, $http, $stateParams, $state) {	
	
	
	
});


/*========== Group controllers ==========*/

// Controller in: group.html
app.controller('GroupCtrl', function($scope, $http, $stateParams) {
	
	// Injects the group object in the parent scope
	/*$http.get('/resource/auth/groups/' + $stateParams.idGroup)
	.then(function(response) {
		$scope.group = response.data;	
	}).finally(function() {
	    // called no matter success or failure
	}); */
	
});

// Controller in: group.general.html
app.controller('GroupGeneralCtrl', function($scope, $http, $stateParams, $mdDialog) {

	// New subgroup dialog
	$scope.addSubgroup = function(ev) {
	    $mdDialog.show({
	      controller: AddSubgroupDialogController,
	      templateUrl: 'add-subgroup-dialog.tmpl.html',
	      parent: angular.element(document.body),
	      targetEvent: ev,
	      clickOutsideToClose: true,
	      fullscreen: true // Only for -xs, -sm breakpoints.
	    })
	    .then(function(answer) {
	      // ok
	    }, function() {
	      // cancel
	    });
	};
	
});

//Controller in: group.activity.html
app.controller('GroupActivityCtrl', function($scope, $http, moment) {

	$http.get('/resource/auth/activities/group/' + $scope.group.idGroup)
		.then(function(response) {
			$scope.activities = response.data;
		}).finally(function() {
			
		});

});

// Controller in: group.management.general.html
app.controller('GroupManagementGeneralCtrl', function($scope, $http, $state, $mdToast, $mdDialog) {

	$scope._group = {};
	$scope._group.name = $scope.group.name;
	$scope._group.description = $scope.group.description;
	$scope._group.reunionDay = $scope.group.reunionDay;
	if ($scope.group.reunionTime) {
		$scope._group.reunionTimeHour = $scope.group.reunionTime.length == 7 ? parseInt($scope.group.reunionTime.substring(0, 1)) :  parseInt($scope.group.reunionTime.substring(0,2));
		$scope._group.reunionTimeMinutes = $scope.group.reunionTime.length == 7 ? parseInt($scope.group.reunionTime.substring(2, 4)) :  parseInt($scope.group.reunionTime.substring(3,5));	
		$scope._group.reunionTimeMeridian = $scope.group.reunionTime.length == 7 ? $scope.group.reunionTime.substring(5, 7) :  $scope.group.reunionTime.substring(6,8);	
	} else {
		$scope._group.reunionTimeHour = null;
		$scope._group.reunionTimeMinutes = null;
		$scope._group.reunionTimeMeridian = null;
	}
	$scope._group.mainUrl = $scope.group.mainUrl;
	$scope._group.secondaryUrl = $scope.group.secondaryUrl;
	$scope._group.photoUrl = $scope.group.photoUrl;
	
	$scope.submit = function() {			
		console.log($scope._group);
		
		// avoids error when minutes are 0
		var parsedMinutes = $scope._group.reunionTimeMinutes === 0 ? $scope._group.reunionTimeMinutes + '0' : $scope._group.reunionTimeMinutes;
		
		// submit data
		$http.put('/resource/auth/groups/' + $scope.group.idGroup, {
			name: $scope._group.name,
			description: $scope._group.description,
			reunionDay: $scope._group.reunionDay,
			reunionTime: $scope._group.reunionTimeHour != null && $scope._group.reunionTimeMinutes != null && $scope._group.reunionTimeMeridian != null ? $scope._group.reunionTimeHour + ":" + parsedMinutes + " " + $scope._group.reunionTimeMeridian : null,
			mainUrl: $scope._group.mainUrl,
			secondaryUrl: $scope._group.secondaryUrl,
			photoUrl: $scope._group.photoUrl	
		}).then(function successCallback(response) {	
			console.log("saved!");
			$mdToast.show(
		      $mdToast.simple()
		        .textContent('Group updated!')
		        .position("bottom right")
		        .hideDelay(1500)
			);
			
			$state.go("group.general", { idGroup: $scope.group.idGroup }, { reload: true });		
		}, function errorCallback(response) {
			$mdToast.show(
		      $mdToast.simple()
		        .textContent('Something went wrong :(')
		        .position("bottom right")
		        .hideDelay(1500)
			);
		});
	};
	  
	$scope.delete = function(ev) {
		var confirm = $mdDialog.confirm()
	        .title('Are you sure you want to delete this group?')
	        .textContent('All of the data including subgroups, workshops and members associated with this instance will be permanently lost.')
	        .ariaLabel('Confirmation')
	        .targetEvent(ev)
	        .ok('Delete')
	        .cancel('Cancel');
		
		$mdDialog.show(confirm)
		.then(function() {
			$http.delete('/resource/auth/groups/' + $scope.group.idGroup)
			.then(function successCallback(response) {
				console.log("deleted!");
				$mdToast.show(
			      $mdToast.simple()
			        .textContent('Group deleted!')
			        .position("bottom right")
			        .hideDelay(1500)
				);
				$state.go("groups", {}, {});
			}, function errorCallback(response) {
				$mdToast.show(
				      $mdToast.simple()
				        .textContent('Something went wrong :(')
				        .position("bottom right")
				        .hideDelay(1500)
					);
			});
	      
	    }, function() {
	      
	    });
	};
	 
});

//Controller in: group.management.members.html
app.controller('GroupManagementMembersCtrl', function($scope, $http, $state, $mdDialog, $mdToast, moment) {
	
	$scope.leave = function(ev) {
		var confirm = $mdDialog.confirm()
        // .title('Are you sure you want to leave this group?')
        .textContent($scope.group.members.length > 1 ? 'Are you sure you want to leave this group?' : 'You are the last member. If you leave, the group will disappear.')
        .ariaLabel('Confirmation')
        .targetEvent(ev)
        .ok('Leave')
        .cancel('Cancel');
	
		$mdDialog.show(confirm)
		.then(function() {
			// submit data
			$http.post('/resource/auth/groups/' + $scope.group.idGroup + "/leave", {
			}).then(function successCallback(response) {	
				// redirect
				$state.go("groups", {}, {reload: true});	
			}, function errorCallback(response) {
				$mdToast.show(
			      $mdToast.simple()
			        .textContent("Something went wrong :(")
			        .position("bottom right")
			        .hideDelay(1500)
				);
			});
	    }, function() {
	      // canceled
	    });		
	};
	
	$scope.nameCoordinator = function(item) {
		// submit data
		$http.post('/resource/auth/groups/' + $scope.group.idGroup + "/name-coordinator", JSON.stringify(item))
		.then(function successCallback(response) {	
			// redirect
			$state.go($state.current, {}, { reload: true });	
		}, function errorCallback(response) {
			$mdToast.show(
		      $mdToast.simple()
		        .textContent("Something went wrong :(")
		        .position("bottom right")
		        .hideDelay(1500)
			);
		});	
	};
	
	$scope.removeMember = function(ev, item) {
		var confirm = $mdDialog.confirm()
        // .title('Are you sure you want to leave this group?')
        .textContent('Are you sure you want to remove this member?')
        .ariaLabel('Confirmation')
        .targetEvent(ev)
        .ok('Remove')
        .cancel('Cancel');
	
		$mdDialog.show(confirm)
		.then(function() {
			// submit data
			$http.post('/resource/auth/groups/' + $scope.group.idGroup + "/delete-member", JSON.stringify(item))
			.then(function successCallback(response) {	
				// redirect
				$state.go($state.current, {}, { reload: true });	
			}, function errorCallback(response) {
				$mdToast.show(
			      $mdToast.simple()
			        .textContent("Something went wrong :(")
			        .position("bottom right")
			        .hideDelay(1500)
				);
			});
	    }, function() {
	      // canceled
	    });		
	};
	
});

// Controller in: group.management.subgroups.html
app.controller('GroupManagementSubgroupsCtrl', function($scope, $http, $state, $mdDialog, $mdToast) {

	// New subgroup dialog
	$scope.addSubgroup = function(ev) {
	    $mdDialog.show({
	      controller: AddSubgroupDialogController,
	      templateUrl: 'add-subgroup-dialog.tmpl.html',
	      parent: angular.element(document.body),
	      targetEvent: ev,
	      clickOutsideToClose: true,
	      fullscreen: true // Only for -xs, -sm breakpoints.
	    })
	    .then(function(answer) {
	      // ok
	    }, function() {
	      // cancel
	    });
	};
	
	$scope.removeSubgroup = function(ev, item) {
		var confirm = $mdDialog.confirm()
        // .title('Are you sure you want to leave this group?')
        .textContent('Are you sure you want to remove this subgroup?')
        .ariaLabel('Confirmation')
        .targetEvent(ev)
        .ok('Remove')
        .cancel('Cancel');
	
		$mdDialog.show(confirm)
		.then(function() {
			// submit data
			$http.delete('/resource/auth/subgroups/' + item.idSubGroup, {})
			.then(function successCallback(response) {	
				// redirect
				$state.go($state.current, {}, { reload: true });	
			}, function errorCallback(response) {
				$mdToast.show(
			      $mdToast.simple()
			        .textContent("Something went wrong :(")
			        .position("bottom right")
			        .hideDelay(1500)
				);
			});
	    }, function() {
	      // canceled
	    });		
	};
	
});



/*========== Subgroup controllers ==========*/

// Controller in: subgroup.html
app.controller('SubgroupCtrl', function($scope, $http, $stateParams, $state) {
	
	// Injects the subgroup object in the parent scope
	$http.get('/resource/auth/subgroups/' + $stateParams.idSubgroup)
		.then(function(response) {	
			$scope.subgroup = response.data;	
		}).finally(function() {
		    // called no matter success or failure
		});
	
});

// Controller in: subgroup.general.html
app.controller('SubgroupGeneralCtrl', function($scope, $http, $stateParams) {
	
	
	
});

// Controller in: subgroup.activity.html
app.controller('SubgroupActivityCtrl', function($scope, $http, moment) {

	$http.get('/resource/auth/activities/subgroup/' + $scope.subgroup.idSubGroup)
		.then(function(response) {
			$scope.activities = response.data;
		}).finally(function() {
			
		});

});

// Controller in: subgroup.management.general.html
app.controller('SubgroupManagementGeneralCtrl', function($scope, $http, $state, $mdToast, $mdDialog) {

	$scope._subgroup = {};
	$scope._subgroup.name = $scope.subgroup.name;
	$scope._subgroup.description = $scope.subgroup.description;
	$scope._subgroup.reunionDay = $scope.subgroup.reunionDay;
	if ($scope.subgroup.reunionTime) {
		$scope._subgroup.reunionTimeHour = $scope.subgroup.reunionTime.length == 7 ? parseInt($scope.subgroup.reunionTime.substring(0, 1)) :  parseInt($scope.subgroup.reunionTime.substring(0,2));
		$scope._subgroup.reunionTimeMinutes = $scope.subgroup.reunionTime.length == 7 ? parseInt($scope.subgroup.reunionTime.substring(2, 4)) :  parseInt($scope.subgroup.reunionTime.substring(3,5));	
		$scope._subgroup.reunionTimeMeridian = $scope.subgroup.reunionTime.length == 7 ? $scope.subgroup.reunionTime.substring(5, 7) :  $scope.subgroup.reunionTime.substring(6,8);
	} else {
		$scope._subgroup.reunionTimeHour = null;
		$scope._subgroup.reunionTimeMinutes = null;
		$scope._subgroup.reunionTimeMeridian = null;
	}
	$scope._subgroup.mainUrl = $scope.subgroup.mainUrl;
	$scope._subgroup.secondaryUrl = $scope.subgroup.secondaryUrl;
	$scope._subgroup.photoUrl = $scope.subgroup.photoUrl;
	
	$scope.submit = function() {			
		console.log($scope._subgroup);
		
		// avoids error when minutes are 0
		var parsedMinutes = $scope._subgroup.reunionTimeMinutes === 0 ? $scope._subgroup.reunionTimeMinutes + '0' : $scope._subgroup.reunionTimeMinutes;
		console.log(parsedMinutes);
		
		// submit data
		$http.put('/resource/auth/subgroups/' + $scope.subgroup.idSubGroup, {
			name: $scope._subgroup.name,
			description: $scope._subgroup.description,
			reunionDay: $scope._subgroup.reunionDay,
			reunionTime: $scope._subgroup.reunionTimeHour != null && $scope._subgroup.reunionTimeMinutes != null && $scope._subgroup.reunionTimeMeridian != null ? $scope._subgroup.reunionTimeHour + ":" + parsedMinutes + " " + $scope._subgroup.reunionTimeMeridian : null,
			mainUrl: $scope._subgroup.mainUrl,
			secondaryUrl: $scope._subgroup.secondaryUrl,
			photoUrl: $scope._subgroup.photoUrl	
		}).then(function successCallback(response) {	
			console.log("saved!");
			$mdToast.show(
		      $mdToast.simple()
		        .textContent('Subgroup updated!')
		        .position("bottom right")
		        .hideDelay(1500)
			);
			
			$state.go("subgroup.general", { idSubGroup: $scope.subgroup.idSubGroup }, { reload: true });
		}, function errorCallback(response) {
			$mdToast.show(
		      $mdToast.simple()
		        .textContent('Something went wrong :(')
		        .position("bottom right")
		        .hideDelay(1500)
			);
		});
	};
	  
	$scope.delete = function(ev) {
		var confirm = $mdDialog.confirm()
	        .title('Are you sure you want to delete this subgroup?')
	        .textContent('All of the data and workshops associated with this instance will be permanently lost.')
	        .ariaLabel('Confirmation')
	        .targetEvent(ev)
	        .ok('Delete')
	        .cancel('Cancel');
		
		$mdDialog.show(confirm)
		.then(function() {
			$http.delete('/resource/auth/subgroups/' + $scope.subgroup.idSubGroup)
			.then(function successCallback(response) {
				console.log("deleted!");
				$mdToast.show(
			      $mdToast.simple()
			        .textContent('Subgroup deleted!')
			        .position("bottom right")
			        .hideDelay(1500)
				);
				$state.go("group.general", { idGroup: $scope.subgroup.groupId }, {});
			}, function errorCallback(response) {
				$mdToast.show(
			      $mdToast.simple()
			        .textContent('Something went wrong :(')
			        .position("bottom right")
			        .hideDelay(1500)
				);
			});
	      
	    }, function() {
	      // canceled
	    });	
	};
	 
});

// Controller in: subgroup.management.members.html
app.controller('SubgroupManagementMembersCtrl', function($scope, $http, $state, $mdDialog, $mdToast, moment) {
	
	$scope.leave = function(ev) {
		var confirm = $mdDialog.confirm()
        // .title('Are you sure you want to leave this group?')
        .textContent($scope.subgroup.members.length > 1 ? 'Are you sure you want to leave this subgroup?' : 'You are the last member. If you leave, the subgroup will disappear.')
        .ariaLabel('Confirmation')
        .targetEvent(ev)
        .ok('Leave')
        .cancel('Cancel');
	
		$mdDialog.show(confirm)
		.then(function() {
			// submit data
			$http.post('/resource/auth/subgroups/' + $scope.subgroup.idSubGroup + "/leave", {
			}).then(function successCallback(response) {	
				// redirect
				$state.go("group.general", { idGroup: $scope.subgroup.groupId }, { reload: true });
			}, function errorCallback(response) {
				$mdToast.show(
			      $mdToast.simple()
			        .textContent("Something went wrong :(")
			        .position("bottom right")
			        .hideDelay(1500)
				);
			});
	    }, function() {
	      // canceled
	    });		
	};
	
	$scope.nameCoordinator = function(item) {
		// submit data
		$http.post('/resource/auth/subgroups/' + $scope.subgroup.idSubGroup + "/name-coordinator", JSON.stringify(item))
		.then(function successCallback(response) {	
			// redirect
			$state.go($state.current, {}, { reload: true });	
		}, function errorCallback(response) {
			$mdToast.show(
		      $mdToast.simple()
		        .textContent("Something went wrong :(")
		        .position("bottom right")
		        .hideDelay(1500)
			);
		});	
	};
	
	$scope.removeMember = function(ev, item) {
		var confirm = $mdDialog.confirm()
        // .title('Are you sure you want to leave this group?')
        .textContent('Are you sure you want to remove this member?')
        .ariaLabel('Confirmation')
        .targetEvent(ev)
        .ok('Remove')
        .cancel('Cancel');
	
		$mdDialog.show(confirm)
		.then(function() {
			// submit data
			$http.post('/resource/auth/subgroups/' + $scope.subgroup.idSubGroup + "/delete-member", JSON.stringify(item))
			.then(function successCallback(response) {	
				// redirect
				$state.go($state.current, {}, { reload: true });	
			}, function errorCallback(response) {
				$mdToast.show(
			      $mdToast.simple()
			        .textContent("Something went wrong :(")
			        .position("bottom right")
			        .hideDelay(1500)
				);
			});
	    }, function() {
	      // canceled
	    });		
	};
	
});

// Controller in: subgroup.management.workshops.html
app.controller('SubgroupManagementWorkshopsCtrl', function($scope, $http, $state, $mdDialog, $mdToast) {

	// New subgroup dialog
	$scope.addSubgroup = function(ev) {
	    $mdDialog.show({
	      controller: AddSubgroupDialogController,
	      templateUrl: 'add-subgroup-dialog.tmpl.html',
	      parent: angular.element(document.body),
	      targetEvent: ev,
	      clickOutsideToClose: true,
	      fullscreen: true // Only for -xs, -sm breakpoints.
	    })
	    .then(function(answer) {
	      // ok
	    }, function() {
	      // cancel
	    });
	};
	
	$scope.removeWorkshop = function(ev, item) {
		var confirm = $mdDialog.confirm()
        // .title('Are you sure you want to leave this group?')
        .textContent('Are you sure you want to remove this workshop?')
        .ariaLabel('Confirmation')
        .targetEvent(ev)
        .ok('Remove')
        .cancel('Cancel');
	
		$mdDialog.show(confirm)
		.then(function() {
			// submit data
			$http.delete('/resource/auth/workshops/' + item.idWorkshop, {})
			.then(function successCallback(response) {	
				// redirect
				$state.go($state.current, {}, { reload: true });	
			}, function errorCallback(response) {
				$mdToast.show(
			      $mdToast.simple()
			        .textContent("Something went wrong :(")
			        .position("bottom right")
			        .hideDelay(1500)
				);
			});
	    }, function() {
	      // canceled
	    });		
	};
	
});

// Controller in: subgroup.add-workshop.html
app.controller('SubgroupAddWorkshopCtrl', function($scope, $http, $stateParams, $state, $window, $mdToast) {
	var self = this; 
	
	$scope.address = {
	    name: '',
	    place: '',
	    components: {
	      placeId: '',
	      streetNumber: '', 
	      street: '',
	      city: '',
	      state: '',
	      countryCode: '',
	      country: '',
	      postCode: '',
	      district: '',
	      location: {
	        lat: '',
	        long: ''
	      }
	    }
	};
	
	// initialize checkbox attributes
	$scope._workshop = {};
	$scope._workshop.isPaid = false;
	// restrict min-date for date pickers
	$scope.today = new Date();
	
	$scope.getAppMatches = function(searchText) {
	    return $http
	      .get("/resource/auth/locations/search/" + searchText)
	      .then(function(response) {
	    	  // Map the response object to the data object.
	    	  return response.data;
	      });
	};
	
	$scope.submit = function() {	
		console.log($scope._workshop);
		 
		// submit data
		$http.post('/resource/auth/workshops/', {
			name: $scope._workshop.name,
			description: $scope._workshop.description,
			startDate: $scope._workshop.startDate.getDate() + "-" + ($scope._workshop.startDate.getMonth() + 1) + "-" + $scope._workshop.startDate.getFullYear(),
			startTime: $scope._workshop.startTimeHours + ":" + $scope._workshop.startTimeMinutes + " " + $scope._workshop.startTimeMeridian,  // time format for api
			endDate: $scope._workshop.endDate.getDate() + "-" + ($scope._workshop.endDate.getMonth() + 1) + "-" + $scope._workshop.endDate.getFullYear(),
			endTime: $scope._workshop.endTimeHours + ":" + $scope._workshop.endTimeMinutes + " " + $scope._workshop.endTimeMeridian, 
			isPaid: $scope._workshop.isPaid,
			price: $scope._workshop.isPaid ? $scope._workshop.price : null,
			currency: $scope._workshop.isPaid ? $scope._workshop.currency : null,
			facebookUrl: $scope._workshop.facebookUrl,
			ticketsUrl: $scope._workshop.ticketsUrl,
			// dependencies
			subGroupId: $stateParams.idSubgroup, // parent
			locationId: !$scope._workshop.isNewLocation ? self.selectedLocation.idLocation : null,
			locationAddress: $scope._workshop.isNewLocation ? $scope.address.components.street + " " + $scope.address.components.streetNumber : null,
			locationCity: $scope._workshop.isNewLocation ? $scope.address.components.city : null,
			locationCountry: $scope._workshop.isNewLocation ? $scope.address.components.country : null,
			locationLatitude: $scope._workshop.isNewLocation ? $scope.address.components.location.lat : null,
			locationLongitude: $scope._workshop.isNewLocation ? $scope.address.components.location.long : null

		}).then(function successCallback(response) {	
			console.log("saved!");
			$mdToast.show(
		      $mdToast.simple()
		        .textContent('Workshop added!')
		        .position("bottom right")
		        .hideDelay(1500)
			);
			
			$state.go("workshop.general", { idWorkshop: response.data.idWorkshop }, {});
		}, function errorCallback(response) {
			$mdToast.show(
		      $mdToast.simple()
		        .textContent('Something went wrong :(')
		        .position("bottom right")
		        .hideDelay(1500)
			);
		});	  
	  };
	  
	  $scope.goBack = function() {
		  $window.history.back();
	  };
	
});




/*========== Workshop controllers ==========*/

// Controller in: workshop.html
app.controller('WorkshopCtrl', function($scope, $http, $stateParams, $state) {
	
	// Injects the workshop object in the parent scope
	$http.get('/resource/auth/workshops/' + $stateParams.idWorkshop)
		.then(function(response) {
			// if user is not member redirect to external page
			if (!response.data.amITutor) {
				$state.go("workshop-out", { idWorkshop: $stateParams.idWorkshop }, {});
			}	
			$scope.workshop = response.data;
		}).finally(function() {
		    // called no matter success or failure
		});
	
});

// Controller in: workshop.general.html
app.controller('WorkshopGeneralCtrl', function($scope, $http, $stateParams) {
	
	
	
});

// Controller in: workshop.management.general.html
app.controller('WorkshopManagementGeneralCtrl', function($scope, $http, $stateParams, $state, $mdDialog, $mdToast) {
	var self = this;
	$scope.address = {
	    name: '',
	    place: '',
	    components: {
	      placeId: '',
	      streetNumber: '', 
	      street: '',
	      city: '',
	      state: '',
	      countryCode: '',
	      country: '',
	      postCode: '',
	      district: '',
	      location: {
	        lat: '',
	        long: ''
	      }
	    }
	};
	
	// restrict min-date for date pickers
	$scope.today = new Date();
	
	$scope._workshop = {};
	$scope._workshop.name = $scope.workshop.name;
	$scope._workshop.description = $scope.workshop.description;
	$scope._workshop.startDate = new Date(parseInt($scope.workshop.startDate.substring(6, 10)), parseInt($scope.workshop.startDate.substring(3, 5)) - 1, parseInt($scope.workshop.startDate.substring(0, 2)));
	$scope._workshop.startTimeHours = $scope.workshop.startTime.length == 7 ? parseInt($scope.workshop.startTime.substring(0, 1)) : parseInt($scope.workshop.startTime.substring(0,2));
	$scope._workshop.startTimeMinutes = $scope.workshop.startTime.length == 7 ? parseInt($scope.workshop.startTime.substring(2, 4)) : parseInt($scope.workshop.startTime.substring(3,5));	
	$scope._workshop.startTimeMeridian = $scope.workshop.startTime.length == 7 ? $scope.workshop.startTime.substring(5, 7) : $scope.workshop.startTime.substring(6,8);
	$scope._workshop.endDate = new Date(parseInt($scope.workshop.endDate.substring(6, 10)), parseInt($scope.workshop.endDate.substring(3, 5)) - 1, parseInt($scope.workshop.endDate.substring(0, 2)));
	$scope._workshop.endTimeHours = $scope.workshop.endTime.length == 7 ? parseInt($scope.workshop.endTime.substring(0, 1)) : parseInt($scope.workshop.endTime.substring(0,2));
	$scope._workshop.endTimeMinutes = $scope.workshop.endTime.length == 7 ? parseInt($scope.workshop.endTime.substring(2, 4)) : parseInt($scope.workshop.endTime.substring(3,5));
	$scope._workshop.endTimeMeridian = $scope.workshop.endTime.length == 7 ? $scope.workshop.endTime.substring(5, 7) : $scope.workshop.endTime.substring(6,8);
	$scope._workshop.isPaid = $scope.workshop.isPaid;
	$scope._workshop.price = $scope.workshop.price;
	$scope._workshop.currency = $scope.workshop.currency;
	$scope._workshop.facebookUrl = $scope.workshop.facebookUrl;
	$scope._workshop.ticketsUrl = $scope.workshop.ticketsUrl;
	
	$scope.getAppMatches = function(searchText) {
	    return $http
	      .get("/resource/auth/locations/search/" + searchText)
	      .then(function(response) {
	    	  // Map the response object to the data object.
	    	  return response.data;
	      });
	};
	
	$scope.submit = function() {			
		console.log($scope._workshop);
		// submit data
		$http.put('/resource/auth/workshops/' + $scope.workshop.idWorkshop, {
			name: $scope._workshop.name,
			description: $scope._workshop.description,
			startDate: $scope._workshop.startDate.getDate() + "-" + ($scope._workshop.startDate.getMonth() + 1) + "-" + $scope._workshop.startDate.getFullYear(),
			startTime: $scope._workshop.startTimeHours + ":" + $scope._workshop.startTimeMinutes + " " + $scope._workshop.startTimeMeridian,  // time format for api
			endDate: $scope._workshop.endDate.getDate() + "-" + ($scope._workshop.endDate.getMonth() + 1) + "-" + $scope._workshop.endDate.getFullYear(),
			endTime: $scope._workshop.endTimeHours + ":" + $scope._workshop.endTimeMinutes + " " + $scope._workshop.endTimeMeridian, 
			isPaid: $scope._workshop.isPaid,
			price: $scope._workshop.isPaid ? $scope._workshop.price : null,
			currency: $scope._workshop.isPaid ? $scope._workshop.currency : null,
			facebookUrl: $scope._workshop.facebookUrl,
			ticketsUrl: $scope._workshop.ticketsUrl,
			// dependencies
			subGroupId: $stateParams.idSubgroup, // parent
			locationId: !$scope._workshop.isNewLocation ? self.selectedLocation.idLocation : null,
			locationAddress: $scope._workshop.isNewLocation ? $scope.address.components.street + " " + $scope.address.components.streetNumber : null,
			locationCity: $scope._workshop.isNewLocation ? $scope.address.components.city : null,
			locationCountry: $scope._workshop.isNewLocation ? $scope.address.components.country : null,
			locationLatitude: $scope._workshop.isNewLocation ? $scope.address.components.location.lat : null,
			locationLongitude: $scope._workshop.isNewLocation ? $scope.address.components.location.long : null
			
		}).then(function successCallback(response) {	
			console.log("saved!");
			$mdToast.show(
		      $mdToast.simple()
		        .textContent('Workshop updated!')
		        .position("bottom right")
		        .hideDelay(1500)
			);

			$state.go("workshop.general", { idWorkshop: $scope.workshop.idWorkshop }, { reload: true });
		}, function errorCallback(response) {
			$mdToast.show(
		      $mdToast.simple()
		        .textContent('Something went wrong :(')
		        .position("bottom right")
		        .hideDelay(1500)
			);
		});
	};
	  
	$scope.delete = function(ev) {
		var confirm = $mdDialog.confirm()
	        .title('Are you sure you want to delete this workshop?')
	        .textContent('All of the data associated with this instance will be permanently lost.')
	        .ariaLabel('Confirmation')
	        .targetEvent(ev)
	        .ok('Delete')
	        .cancel('Cancel');
		
		$mdDialog.show(confirm)
		.then(function() {
			$http.delete('/resource/auth/workshops/' + $scope.workshop.idWorkshop)
			.then(function successCallback(response) {
				console.log("deleted!");
				$mdToast.show(
			      $mdToast.simple()
			        .textContent('Workshop deleted!')
			        .position("bottom right")
			        .hideDelay(1500)
				);
				
				$state.go("subgroup.general", { idSubgroup: $scope.workshop.subGroupId }, {});
			}, function errorCallback(response) {
				$mdToast.show(
			      $mdToast.simple()
			        .textContent('Something went wrong :(')
			        .position("bottom right")
			        .hideDelay(1500)
				);
			});
	      
	    }, function() {
	      // canceled
	    });	
	};
	 
});




/*========== Dialog controllers ==========*/

// Controller for: add-group-dialog.tmpl.html
function AddGroupDialogController($scope, $mdDialog, $http, $state, $mdToast) {
	 
	$scope.actionsDisabled = false;
	
	$scope.hide = function() {
	    $mdDialog.hide();
	};
	
	$scope.cancel = function() {
	    $mdDialog.cancel();
	};
	
	$scope.submit = function() {	
		console.log($scope._group);
		// submit data
		$http.post('/resource/auth/groups/', {
			name: $scope._group.name,
			description: $scope._group.description
		}).then(function successCallback(response) {	
			console.log("saved!");
			$mdToast.show(
		      $mdToast.simple()
		        .textContent('Group added!')
		        .position("bottom right")
		        .hideDelay(1500)
			);
			// pass data retrieved to parent controller
			$mdDialog.hide(response.data);
			// go to group state
			$state.go("group.general", { idGroup: response.data.idGroup }, {});
		}, function errorCallback(response) {
			$mdToast.show(
		      $mdToast.simple()
		        .textContent('Something went wrong :(')
		        .position("bottom right")
		        .hideDelay(1500)
			);
		});	  
	  };
};

// Controller for: add-subgroup-dialog.tmpl.html
function AddSubgroupDialogController($scope, $mdDialog, $http, $stateParams, $state, $mdToast) {
	 
	$scope.actionsDisabled = false;
	console.log($stateParams.idGroup);
	
	$scope.hide = function() {
	    $mdDialog.hide();
	};
	
	$scope.cancel = function() {
	    $mdDialog.cancel();
	};
	
	$scope.submit = function() {			
		console.log($scope._subgroup);
		// submit data
		$http.post('/resource/auth/subgroups', {
			name: $scope._subgroup.name,
			description: $scope._subgroup.description,
			groupId: $stateParams.idGroup // parent
		}).then(function successCallback(response) {	
			console.log("saved!");
			$mdToast.show(
		      $mdToast.simple()
		        .textContent('Subgroup added!')
		        .position("bottom right")
		        .hideDelay(1500)
			);
			// pass data retrieved to parent controller
			$mdDialog.hide(response.data);	
			// reload current state
			$state.go("subgroup.general", { idSubgroup: response.data.idSubGroup }, {});
		}, function errorCallback(response) {
			$mdToast.show(
		      $mdToast.simple()
		        .textContent('Something went wrong :(')
		        .position("bottom right")
		        .hideDelay(1500)
			);
		});
	  };
};


//================= Custom filters =====================

app.filter('UTCToNow', ['moment', function (moment) {
    return function (input, format) {
       if(format) {
    	   return moment.utc(input).local().format('dddd, MMMM Do YYYY, h:mm:ss a');
       }
       else {
    	   return moment.utc(input).local();
       }
    };
}]);


//================= Custom directives =====================

// Defining a custom directive for handling the user avatar
// extracted from: http://plnkr.co/edit/UHq23coTUSrwnMKq1Itv?p=preview
app.directive('userAvatar', ["avatarService", function (avatarService) {
	var controller = function ($scope) {		
		$scope.$watch("mFabber", function(newValue, oldValue, scope) {
			if (newValue) {
				$scope.imageAvailable = false;
				if (!$scope.mFabber.avatar) {
					$scope.genericAvatar = avatarService.getAvatar($scope.mFabber);
				} else {
					$scope.imageAvailable = true;
				}	
			}
        });
	};
	return {
		restrict: 'E',
		scope: {
			mFabber: '=fabber',
			avatarWidth: '@avatarW',
			avatarHeight: '@avatarH',
			avatarFontSize: '@avatarFontSize'
		},
		template: '<div class="generic-avatar" style="width: {{avatarWidth}}px; height: {{avatarHeight}}px;">' +
			'<div class="avatar-circle" style="background-color: {{genericAvatar.background}};"></div>' +
			'<span class="name" style="font-size: {{avatarFontSize}}px;">{{genericAvatar.initials}}</span>' +
			'<div class="img-avatar" data-ng-if="imageAvailable" style="background-image: url({{mFabber.avatar}})"></div>' +
			'</div>',
		controller: controller
	};
}])
.factory("avatarService", function(){
    var avatarService = function(fabber) {
    	var colorCodes = ["#1abc9c", "#2ecc71", "#3498db", "#9b59b6", "#34495e", "#16a085", "#27ae60", 
    	                  "#2980b9", "#8e44ad", "#2c3e50", "#f1c40f", "#e67e22", "#e74c3c", "#95a5a6", 
    	                  "#f39c12", "#d35400", "#c0392b", "#bdc3c7", "#7f8c8d"];
      
		var i1 = "", i2 = "", nameArray = [];
		if (angular.isDefined(fabber.name)) {
			i1 = angular.uppercase(fabber.name.charAt(0));
			nameArray = fabber.name.split(" ");
			if (nameArray.length > 2) {
				i2 = angular.uppercase(nameArray[nameArray.length - 1].charAt(0));
			} else {
				i2 = angular.uppercase(nameArray[1].charAt(0));
			}
		} else {
			i1 = angular.uppercase(fabber.firstName.charAt(0));
			nameArray = fabber.lastName.split(" ");
			if (nameArray.length > 2) {
				i2 = nameArray[nameArray.length - 1].charAt(0);
			} else {
				i2 = angular.uppercase(nameArray[0].charAt(0));
			}
		}
		var initials = i1 + "" + i2;
		var charIndex = initials.charCodeAt(0) - 48,
	    	colourIndex = charIndex % 19;
		
		var background = colorCodes[colourIndex];
		return ({ "initials": initials, "background": background });
    }
    return {
      getAvatar: avatarService
    }
});

// Group avatar
app.directive('groupAvatar', ["groupAvatarService", function (groupAvatarService) {
	var controller = function ($scope) {		
		$scope.$watch("mGroup", function(newValue, oldValue, scope) {
			if (newValue) {
				$scope.imageAvailable = false;
				if (!$scope.mGroup.avatar) {
					$scope.genericAvatar = groupAvatarService.getAvatar($scope.mGroup);
				} else {
					$scope.imageAvailable = true;
				}	
			}
        });
	};
	return {
		restrict: 'E',
		scope: {
			mGroup: '=group',
			avatarWidth: '@avatarW',
			avatarHeight: '@avatarH',
			avatarFontSize: '@avatarFontSize'
		},
		template: '<div class="generic-avatar" style="width: {{avatarWidth}}px; height: {{avatarHeight}}px;">' +
			'<div class="avatar-circle" style="background-color: {{genericAvatar.background}};"></div>' +
			'<span class="name" style="font-size: {{avatarFontSize}}px;">{{genericAvatar.initials}}</span>' +
			'<div class="img-avatar" data-ng-if="imageAvailable" style="background-image:url({{mFabber.avatar}})"></div>' +
			'</div>',
		controller: controller
	};
}])
.factory("groupAvatarService", function() {
    var groupAvatarService = function(group) {
    	var colorCodes = ["#1abc9c", "#2ecc71", "#3498db", "#9b59b6", "#34495e", "#16a085", "#27ae60", 
    	                  "#2980b9", "#8e44ad", "#2c3e50", "#f1c40f", "#e67e22", "#e74c3c", "#95a5a6", 
    	                  "#f39c12", "#d35400", "#c0392b", "#bdc3c7", "#7f8c8d"];
      
		var i1 = "", nameArray = [];
		
		i1 = angular.uppercase(group.name.charAt(0));
		
		var initials = i1;
		var charIndex = initials.charCodeAt(0) - 48,
	    	colourIndex = charIndex % 19;
		
		var background = colorCodes[colourIndex];
		return ({ "initials": initials, "background": background });
    }
    return {
      getAvatar: groupAvatarService
    }
});
