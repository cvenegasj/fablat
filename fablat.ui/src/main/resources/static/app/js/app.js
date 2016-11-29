var app = angular.module('FabLatApp', [ 'ui.router', 'ngMaterial', 'ng.group', 'sasrio.angular-material-sidenav', 'ngMessages' ]);

app.config(function($mdThemingProvider, $urlRouterProvider, $stateProvider, ssSideNavSectionsProvider) {

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
        name: 'home',
        url: '/',
        templateUrl: 'dashboard.html',
        controller: function ($rootScope) {
            $rootScope.model = {
                title: 'Dashboard'
            };
        }
    });

    $stateProvider.state({
        name: 'dashboard',
        url: '/dashboard',
        templateUrl: 'dashboard.html',
        controller: function ($rootScope) {
            $rootScope.model = {
                title: 'Dashboard'
            };
        }
    });
    
    $stateProvider.state({
        name: 'projects',
        url: '/project-manager',
        templateUrl: 'project-manager.html',
        controller: function ($rootScope) {
        	$rootScope.model = {
                title: 'Projects'
            };
        }
    });

    $stateProvider.state({
        name: 'replications',
        url: '/replication-manager',
        templateUrl: 'replication-manager.html',
        controller: function ($rootScope) {
        	$rootScope.model = {
                title: 'Replications'
            };
        }
    });
    
    $stateProvider.state({
        name: 'personalData',
        url: '/personal-data',
        templateUrl: 'personal-data.html',
        controller: function ($rootScope) {
        	$rootScope.model = {
                title: 'Personal Data'
            };
        }
    });
    
    $stateProvider.state({
        name: 'projectList',
        url: '/project/list',
        templateUrl: 'project-list.html',
        controller: function ($rootScope) {
        	$rootScope.model = {
                title: 'Project List'
            };
        }
    });
    
    $stateProvider.state({
        name: 'projectDetail',
        url: '/project/detail/:idProject',
        templateUrl: 'project-detail.html',
        controller: function ($rootScope) {
        	$rootScope.model = {
                title: 'Project Detail'
            };
        }
    });
    
    $stateProvider.state({
        name: 'workshopDetail',
        url: '/workshop/detail/:idWorkshop',
        templateUrl: 'workshop-detail.html',
        controller: function ($rootScope) {
        	$rootScope.model = {
                title: 'Workshop Detail'
            };
        }
    });
    
    $stateProvider.state({
        name: 'fabberDetail',
        url: '/fabber/detail/:idFabber',
        templateUrl: 'fabber-detail.html',
        controller: function ($rootScope) {
        	$rootScope.model = {
                title: 'Fabber Detail'
            };
        }
    });
    
    $stateProvider.state({
        name: 'generalManagerLat',
        url: '/admin-lat/general-manager',
        templateUrl: 'admin-lat-general-manager.html',
        controller: function ($rootScope) {
        	$rootScope.model = {
                title: 'General Manager Lat'
            };
        }
    });
    
    $stateProvider.state({
        name: 'generalManagerLab',
        url: '/admin-lab/general-manager',
        templateUrl: 'admin-lab-general-manager.html',
        controller: function ($rootScope) {
        	$rootScope.model = {
                title: 'General Manager Lab'
            };
        }
    });
    
    // Sidenav structure
    
    ssSideNavSectionsProvider.initWithTheme($mdThemingProvider);
    ssSideNavSectionsProvider.initWithSections([{
        id: 'section1',
        name: 'General',
        type: 'heading',
        children: [{
            id: 'dashboard',
            name: 'Dashboard',
            state: 'dashboard',
            type: 'link'
        }, {
            id: 'projects',
            name: 'Projects',
            state: 'projects',
            type: 'link'
        }, {
            id: 'replications',
            name: 'Replications',
            state: 'replications',
            type: 'link'
        }]
    }, {
        id: 'section2',
        name: 'Account',
        type: 'heading',
        children: [{
            id: 'personal_data',
            name: 'Personal Data',
            state: 'personalData',
            type: 'link'
        }]
    }, {
        id: 'section3',
        name: 'Management',
        type: 'heading',
        hidden: true,
        children: [{
            id: 'general_manager_lat',
            name: 'General Manager Lat',
            state: 'generalManagerLat',
            type: 'link',
            hidden: true
        }, {
            id: 'general_manager_lab',
            name: 'General Manager Lab',
            state: 'generalManagerLab',
            type: 'link',
            hidden: true
        }, {
            id: 'data_visualization',
            name: 'Data visualization',
            state: 'dataVisualization',
            type: 'link'
        }]
    }
    ]);
});

// General controller, runs on top of everything
app.controller('AppCtrl', ['$rootScope', '$http', '$location', '$window', '$scope', '$mdBottomSheet','$mdSidenav', '$mdDialog', '$timeout', 'ssSideNav', function($rootScope, $http, $location, $window, $scope, $mdBottomSheet, $mdSidenav, $mdDialog, $timeout, ssSideNav) { 

	// Sidenav menu
	$scope.menu = ssSideNav;
	
	$rootScope.user = {};
	
	$http.get('/resource/fabber/user-diggested').then(function(response) {
		if (response.data) {
			$rootScope.authenticated = true;
			$rootScope.user.id = response.data.id;
			$rootScope.user.username = response.data.username;
			$rootScope.user.email = response.data.email;
			$rootScope.user.firstName = response.data.firstName;
			$rootScope.user.lastName = response.data.lastName;
			$rootScope.user.idLab = response.data.idLab;
			$rootScope.user.roles = response.data.roles;
			
			console.log("authenticated");
			console.log("id: " + $rootScope.user.id);
			console.log("username: " + $rootScope.user.username);
			console.log("email: " + $rootScope.user.email);
			console.log("lab: " + $rootScope.user.idLab); // get value of first lab in list
			
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
			
			
		    // Show or Hide menus by Roles
			if ($rootScope.user.roles.indexOf("ROLE_ADMIN_LAT") !== -1) {
				ssSideNav.setVisibleFor([{
					id: 'section3',
					value: true
				}, {
					id: 'general_manager_lat',
					value: true
				}]);
			}
			
			if ($rootScope.user.roles.indexOf("ROLE_ADMIN_LAB") !== -1) {
				ssSideNav.setVisibleFor([{
					id: 'section3',
					value: true
				}, {
					id: 'general_manager_lab',
					value: true
				}]);
			}
			
		} else {
			$rootScope.authenticated = false;
			console.log("not authenticated");
			// redirect to login page
			$window.location.href = '/login';
		}
	}, function() {
		$rootScope.authenticated = false;
		console.log("error in authentication.");
		$window.location.href = '/login'; // redirect to login page
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
	  
	  	
    /*ssSideNav.setVisible('link_1');
    ssSideNav.setVisibleFor([{
      id: 'toggle_item_1',
      value: true
    }, {
      id: 'link_1',
      value: false
    }]);

    $timeout(function () {
      ssSideNav.setVisible('toogle_2', false);
    });

    $timeout(function () {
        // force selection on child dropdown menu item and select its state too.
        ssSideNav.forceSelectionWithId('toogle_1_link_2');
    }, 1000 * 3);*/
    
    //***************

}]);


//Controller for dashboard view: dashboard.html
app.controller('DashboardCtrl', function($rootScope, $scope, $http, $filter) {
	
	$http.get('/resource').then(function(response) {
		$scope.greeting = response.data;
	});
	
	// $scope.loading1 = true;
	$scope.loading2 = true;
	$scope.loading3 = true;
	$scope.loading4 = true;
	
	$http.get('/resource/fabber/current').then(function(response) {
		$scope.fabber = response.data;
	}).finally(function() {
	    // called no matter success or failure
	    // $scope.loading1 = false;
	});
	
	$http.get('/resource/fabber/current-stats').then(function(response) {
		$scope.stats = response.data;
	}).finally(function() {
	    // called no matter success or failure
	    $scope.loading2 = false;
	});
	
	$http.get('/resource/project/list-exclusive-user').then(function(response) {
		$scope.projects = response.data;
	}).finally(function() {
	    // called no matter success or failure
	    $scope.loading3 = false;
	});
	
	$http.get('/resource/workshop/list-upcoming-user').then(function(response) {
		$scope.workshops = response.data;
	}).finally(function() {
	    // called no matter success or failure
	    $scope.loading4 = false;
	});
	
});


// Controller for projects list: project-list.html
app.controller('ProjectListCtrl', function($scope, $http, $mdDialog) {
	
	$scope.loading = true;
	
	$http.get('/resource/project/list-all-user').then(function(response) {
		$scope.projects = response.data;
	}).finally(function() {
	    // called no matter success or failure
	    $scope.loading = false;
	});
	
	$scope.join = function(idProject, $index) {
		$http.post('/resource/project/join', {
			idProject: idProject
		}).then(function(response) {
			// toggle status
			$scope.projects[$index].currentUserStatus = "JOINED";
		});
	}
	
});


// Controller for project manager view: project-manager.html
app.controller('ProjectManagerCtrl', function($scope, $http, $mdDialog, $mdMedia) {
	
	$scope.loading1 = true;
	$scope.loading2 = true;
	$scope.customFullscreen = $mdMedia('xs') || $mdMedia('sm');
	
	$scope.openMenu = function($mdOpenMenu, ev) {
		$mdOpenMenu(ev);
	};
	
	$scope.newProject = function(ev) {
	    var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))  && $scope.customFullscreen;
	    $mdDialog.show({
	      controller: NewProjectDialogController,
	      templateUrl: 'new-project-dialog.tmpl.html',
	      parent: angular.element(document.body),
	      targetEvent: ev,
	      clickOutsideToClose:true,
	      fullscreen: useFullScreen
	    })
	    .then(function(model) {
	    	// alert("troll :D");
	    	console.log(model);
	    	$scope.projects1.push(model);
	    	
	    }, function() {
	      // cancelled dialog
	    });
	    
	    $scope.$watch(function() {
	      return $mdMedia('xs') || $mdMedia('sm');
	    }, function(wantsFullScreen) {
	      $scope.customFullscreen = (wantsFullScreen === true);
	    });
	};
	
	// get managed projects
	$http.get('/resource/project/list-managed-user').then(function(response) {
		$scope.projects1 = response.data;
	}).finally(function() {
	    // called no matter success or failure
	    $scope.loading1 = false;
	});
	
	// get not managed projects
	$http.get('/resource/project/list-not-managed-user').then(function(response) {
		$scope.projects2 = response.data;
	}).finally(function() {
	    // called no matter success or failure
	    $scope.loading2 = false;
	});
	
	$scope.leaveProject = function(event, idProject, $index) {
		
		$mdDialog.confirm()
	        .title('Are you sure you want to leave this project?')
			.textContent('You can still join the project later.')
			.ariaLabel('Leave confirmation')
			.targetEvent(event)
			.ok('Leave')
			.cancel('Cancel');
		$mdDialog.show(confirm).then(function() {
			$scope.status = 'You left the project.';
			
			/*$http.post('resource/group/leave', {
				idGroup: idGroup
			}).then(function(response) {
				// delete item from ng model
				
			});*/
		
		  }, function() {
		    $scope.status = 'You canceled the action.';
		  });
	}
	
	$scope.toggleNotifications = function(idProject, $index) {
		$http.post('/resource/project/toggle-notifications', {
			idProject: idProject
		}).then(function(response) {
			
		
		});
	}
	
});


// Controller within: project-manager.html
app.controller('ProjectsAllCtrl', function($scope, $http) {
	
	$scope.loading = true;
	
	$http.get('/resource/project/list-all-user').then(function(response) {
		
		$scope.projects = response.data;
		
	}).finally(function() {
	    // called no matter success or failure
	    $scope.loading = false;
	});
	
	$scope.join = function(idProject, $index) {
		$http.post('/resource/project/join', {
			idProject: idProject
		}).then(function(response) {
			// toggle status
			$scope.projects[$index].currentUserStatus = "JOINED";
		});
	}
	
});


// Controller for project detail view: project-detail.html
app.controller('ProjectDetailCtrl', function($scope, $http, $stateParams, $state) {
	
	$scope.openMenu = function($mdOpenMenu, ev) {
		$mdOpenMenu(ev);
	};
	
	$scope.loading = true;
	
	$http.get('/resource/project/detail', {
		params: {
			idProject: $stateParams.idProject
		}
	}).then(function(response) {		
		$scope.project = response.data;		
	}).finally(function() {
	    // called no matter success or failure
	    $scope.loading = false;
	});
	
	// for getting current user status
	$http.get('/resource/project/get-current-user-status', {
		params: {
			idProject: $stateParams.idProject
		}
	}).then(function(response) {
		
		$scope.isProjectMember = response.data.isProjectMember;
		$scope.isCoordinator = response.data.isCoordinator;
		
	});
	
	$scope.joinProject = function() {
		$http.post('/resource/project/join', {
			idProject: $stateParams.idProject
		}).then(function(response) {
			// reload state
			$state.go($state.current, {}, {reload: true});
		});
	}
	
	$scope.deleteFromProject = function(idFabber, $index, isCoordinator) {
		$http.post('/resource/project/delete-user', {
			idProject: $stateParams.idProject,
			idFabber: idFabber
		}).then(function(response) {
			if (isCoordinator) {
				$scope.project.coordinators.splice($index, 1);
			} else {
				$scope.project.collaborators.splice($index, 1);
			}			
		});
	}
	
	$scope.nameAsCoordinator = function(idFabber, $index) {
		$http.post('/resource/project/name-coordinator', {
			idProject: $stateParams.idProject,
			idFabber: idFabber
		}).then(function(response) {
			// delete from view model
			$scope.project.collaborators.splice($index, 1);
			$scope.project.coordinators.push(response.data);
		});
	}
	
});


// Controller within: project-detail.html
app.controller('ProjectReplicationsCtrl', function($scope, $http, $stateParams, $mdDialog, $mdMedia) {
	
	$scope.loading = true;
	
	// Get project's workshops
	$http.get('/resource/project/list-upcoming-workshops', {
		params: {
			idProject: $stateParams.idProject
		}
	}).then(function(response) {
		
		$scope.workshops = response.data;
		
	}).finally(function() {
	    // called no matter success or failure
	    $scope.loading = false;
	});
	
	// For getting the current user state
	$http.get('/resource/project/detail', {
		params: {
			idProject: $stateParams.idProject
		}
	}).then(function(response) {
		
		$scope.project = response.data;
		
	}).finally(function() {
	    // called no matter success or failure
	    $scope.loading = false;
	});
	
	// Dialog 
	$scope.customFullscreen = $mdMedia('xs') || $mdMedia('sm');
	
	$scope.openMenu = function($mdOpenMenu, ev) {
		$mdOpenMenu(ev);
	};
	
	$scope.newWorkshop = function(ev) {
	    var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))  && $scope.customFullscreen;
	    $mdDialog.show({
	      controller: NewWorkshopDialogController,
	      templateUrl: 'new-workshop-dialog.tmpl.html',
	      parent: angular.element(document.body),
	      targetEvent: ev,
	      clickOutsideToClose:true,
	      fullscreen: useFullScreen
	    })
	    .then(function(model) {
	    	// alert("troll :D");
	    	console.log(model);
	    	$scope.workshops.push(model);
	    	
	    }, function() {
	      // cancelled dialog
	    });
	    
	    $scope.$watch(function() {
	      return $mdMedia('xs') || $mdMedia('sm');
	    }, function(wantsFullScreen) {
	      $scope.customFullscreen = (wantsFullScreen === true);
	    });
	};
	
});


// Controller for replication manager view: replication-manager.html
app.controller('ReplicationManagerCtrl', function($scope, $http) {
	
	$scope.openMenu = function($mdOpenMenu, ev) {
		$mdOpenMenu(ev);
	};
	
	$scope.loading = true;
	
	// Get all upcoming workshops
	$http.get('/resource/workshop/list-upcoming').then(function(response) {
		$scope.workshops = response.data;
	}).finally(function() {
	    // called no matter success or failure
	    $scope.loading = false;
	});
	
});


// Controller within: replication-manager.html
app.controller('YourReplicationsCtrl', function($scope, $http) {
	
	$scope.openMenu = function($mdOpenMenu, ev) {
		$mdOpenMenu(ev);
	};
	
	$scope.loading1 = true;
	
	// Get your upcoming workshops
	$http.get('/resource/workshop/list-upcoming-user').then(function(response) {
		$scope.workshops = response.data;
	}).finally(function() {
	    // called no matter success or failure
	    $scope.loading1 = false;
	});
	
});


// Controller within: replication-manager.html 
app.controller('YourPastReplicationsCtrl', function($scope, $http) {
	
	$scope.openMenu = function($mdOpenMenu, ev) {
		$mdOpenMenu(ev);
	};
	
	$scope.loading1 = true;
	
	// Get your upcoming workshops
	$http.get('/resource/workshop/list-past-user').then(function(response) {
		$scope.workshops = response.data;
	}).finally(function() {
	    // called no matter success or failure
	    $scope.loading1 = false;
	});
	
});


// Controller for workshop detail view: workshop-detail.html
app.controller('WorkshopDetailCtrl', function($scope, $http, $stateParams, $state) {
	
	$scope.openMenu = function($mdOpenMenu, ev) {
		$mdOpenMenu(ev);
	};
	
	$scope.loading = true;
	
	$http.get('/resource/workshop/detail', {
		params: {
			idWorkshop: $stateParams.idWorkshop
		}
	}).then(function(response) {
		
		$scope.workshop = response.data;
		
	}).finally(function() {
	    // called no matter success or failure
	    $scope.loading = false;
	});
	
	// for getting current user status
	$http.get('/resource/workshop/get-current-user-status', {
		params: {
			idWorkshop: $stateParams.idWorkshop
		}
	}).then(function(response) {
		
		$scope.isProjectMember = response.data.isProjectMember;
		$scope.isWorkshopMember = response.data.isWorkshopMember;
		$scope.isCoordinator = response.data.isCoordinator;
		
	});
	
	$scope.joinWorkshop = function() {
		$http.post('/resource/workshop/join', {
			idWorkshop: $stateParams.idWorkshop
		}).then(function(response) {
			// reload state
			$state.go($state.current, {}, {reload: true});
		});
	}
	
	$scope.leaveWorkshop = function() {
		$http.post('/resource/workshop/leave', {
			idWorkshop: $stateParams.idWorkshop
		}).then(function(response) {
			// reload state
			$state.go($state.current, {}, {reload: true});
		});
	}
	
	$scope.deleteFromWorkshop = function(idFabber, $index, isCoordinator) {
		$http.post('/resource/workshop/delete-user', {
			idWorkshop: $stateParams.idWorkshop,
			idFabber: idFabber
		}).then(function(response) {
			if (isCoordinator) {
				$scope.workshop.coordinators.splice($index, 1);
			} else {
				$scope.workshop.collaborators.splice($index, 1);
			}			
		});
	}
	
	$scope.nameAsCoordinator = function(idFabber, $index) {
		$http.post('/resource/workshop/name-coordinator', {
			idWorkshop: $stateParams.idWorkshop,
			idFabber: idFabber
		}).then(function(response) {
			// delete from view model
			$scope.workshop.collaborators.splice($index, 1);
			$scope.workshop.coordinators.push(response.data);
		});
	}
});


//Controller for fabber detail view: fabber-detail.html
app.controller('FabberDetailCtrl', function($rootScope, $scope, $http, $filter, $stateParams) {
	
	$scope.loading2 = true;
	
	$http.get('/resource/fabber/detail', {
		params: {
			idFabber: $stateParams.idFabber
		}
	}).then(function(response) {		
		$scope.fabber = response.data;		
	}).finally(function() {
	    // called no matter success or failure
	    //$scope.loading1 = false;
	});
	
	$http.get('/resource/fabber/stats', {
		params: {
			idFabber: $stateParams.idFabber
		}
	}).then(function(response) {		
		$scope.stats = response.data;		
	}).finally(function() {
	    // called no matter success or failure
	    $scope.loading2 = false;
	});
	
});


// Controller for Personal Data view: personal-data.html
app.controller('PersonalDataCtrl', function($rootScope, $scope, $http) {
	
	$scope.loading1 = true;
	$scope.loading2 = false;
	
	$http.get('/resource/fabber/current').then(function(response) {
		$scope.fabber = response.data;
	}).finally(function() {
	    // called no matter success or failure
	    $scope.loading1 = false;
	});
	
	$scope.updateData = function() {
		$scope.loading2 = true;
		
		
		// When updating personal data we also fetch the data from fablabs api in case that changes have been made
		/* $http.get('/user').then(function(response) {			
			$rootScope.user.id = response.data.id;
			$rootScope.user.username = response.data.login;
			$rootScope.user.email = response.data.email;
			// TODO: to be replaced by real values from fablabs.io API
			$rootScope.user.firstName = response.data.login;
			$rootScope.user.lastName = response.data.login;
			$rootScope.user.idLab = 62;			
		}).finally(function() {
			
		}); */	
		
		$http.post('/resource/fabber/update', {
			firstName: $scope.fabber.firstName,
			lastName: $scope.fabber.lastName,
			// idLab: $scope.fabber.idLab,
			isFabAcademyGrad: $scope.fabber.isFabAcademyGrad,
			fabAcademyGradYear: $scope.fabber.fabAcademyGradYear
		}).then(function(response) {
			$scope.loading2 = false;
			$scope.result = "Data updated correctly.";
		});
	}
	
});


// Controller within: admin-general-manager.html
app.controller('AdminLabManagerCtrl', function($scope, $http, $state) {
	
	$scope.loading = false;
	$scope.loading1 = true;
	
	$scope.updateLabs = function() {		
		$scope.loading = true;
		
		$http.get('https://api.fablabs.io/v0/labs.json').then(function(response) {
			
			$http.post('/resource/lab/update-all', {
				labs: response.data.labs
			}).then(function(response) {
				$scope.loading = false;
				// reload state
				$state.go($state.current, {}, {reload: true});
			});
			
		}); 
	}	
	
	$http.get('/resource/lab/list').then(function(response) {
		$scope.labs = response.data;
	}).finally(function() {
	    // called no matter success or failure
	    $scope.loading1 = false;
	});
	
});


// Controller within: admin-general-manager.html
app.controller('AdminProjectManagerCtrl', function($scope, $http) {
	
	$scope.loading = true;
	
	$scope.openMenu = function($mdOpenMenu, ev) {
		$mdOpenMenu(ev);
	};
	
	// get all groups
	$http.get('/resource/project/list-all-admin').then(function(response) {
		$scope.projects = response.data;
	}).finally(function() {
	    // called no matter success or failure
	    $scope.loading = false;
	});
	
	$scope.deleteProject = function(idProject, $index) {		
		$http.post('/resource/project/delete', {
			idProject: idProject
		}).then(function(response) {
			// update ui model
			$scope.projects.splice($index, 1);
		});
	}	
	
});


//Controller within: admin-general-manager.html
app.controller('AdminReplicationManagerCtrl', function($scope, $http) {
	
	$scope.loading = true;
	
	$scope.openMenu = function($mdOpenMenu, ev) {
		$mdOpenMenu(ev);
	};
	
	// get all upcoming workshops
	$http.get('/resource/workshop/list-upcoming-admin').then(function(response) {
		$scope.workshops = response.data;
	}).finally(function() {
	    // called no matter success or failure
	    $scope.loading = false;
	});
	
	$scope.deleteWorkshop = function(idWorkshop, $index) {		
		$http.post('/resource/workshop/delete', {
			idWorkshop: idWorkshop
		}).then(function(response) {
			// update ui model
			$scope.workshops.splice($index, 1);
		});
	}	
	
});


// ================= Extra controllers =====================

app.controller('ListBottomSheetCtrl', function($scope, $mdBottomSheet) {
	  $scope.items = [
	    { name: 'Share', icon: 'social:ic_share_24px' },
	    { name: 'Upload', icon: 'file:ic_cloud_upload_24px' },
	    { name: 'Copy', icon: 'content:ic_content_copy_24px' },
	    { name: 'Print this page', icon: 'action:ic_print_24px' },
	  ];
	  
	  $scope.listItemClick = function($index) {
	    var clickedItem = $scope.items[$index];
	    $mdBottomSheet.hide(clickedItem);
	  };
});


function DialogController($scope, $mdDialog) {
	  $scope.hide = function() {
	    $mdDialog.hide();
	  };
	  $scope.cancel = function() {
	    $mdDialog.cancel();
	  };
	  $scope.answer = function(answer) {
	    $mdDialog.hide(answer);
	  };
};


// Controller for: new-project-dialog.tmpl.html
function NewProjectDialogController($scope, $mdDialog, $http) {
	 
	$scope.actionsDisabled = false;
	
	$scope.hide = function() {
	    $mdDialog.hide();
	  };
	$scope.cancel = function() {
	    $mdDialog.cancel();
	  };
	$scope.submit = function() {	
		$scope.actionsDisabled = true;
		  //TODO: validate fields
		  
		
		  console.log($scope.project);
		  // submit data
		  $http.post('/resource/project/save', {
			name: $scope.project.name,
			idGroup: $scope.project.group.idGroup,
			description: $scope.project.description,
			reunionDay: $scope.project.reunionDay,
			reunionTime: $scope.project.reunionTime
		}).then(function(response) {	
			console.log("saved!");
			// pass data retrieved to parent controller
			$mdDialog.hide(response.data);	
		});
				  
	  };
	  
	  // get groups
	  $http.get('/resource/group/list').then(function(response) {
			$scope.groups = response.data;
	  });
};


// Controller for: new-workshop-dialog.tmpl.html
function NewWorkshopDialogController($scope, $mdDialog, $http, $stateParams) {
	 
	$scope.actionsDisabled = false;
	$scope.workshop = {};
	$scope.workshop.isPaid = false;
	$scope.workshop.idProject = $stateParams.idProject;
	
	// for getting the project name and the next replication number
	$http.get('/resource/project/brief', {
		params: {
			idProject: $stateParams.idProject
		}
	}).then(function(response) {
		$scope.project = response.data;
	});
	
	$scope.hide = function() {
	    $mdDialog.hide();
	  };
	$scope.cancel = function() {
	    $mdDialog.cancel();
	  };
	$scope.submit = function() {	
		$scope.actionsDisabled = true;
		
		var dateF = $scope.workshop.date.getDate() + "/" + ($scope.workshop.date.getMonth() + 1) + "/" + $scope.workshop.date.getFullYear();
		console.log(dateF);
		// submit data
		$http.post('/resource/workshop/save', {
			name: $scope.workshop.name,
			date: dateF,
			time: $scope.workshop.time,
			isPaid: $scope.workshop.isPaid,
			price: $scope.workshop.price,
			currency: $scope.workshop.currency,
			coordinators: $scope.coordinators,
			collaborators: $scope.collaborators,
			locations: $scope.locations,
			idProject: $scope.workshop.idProject
		}).then(function(response) {	
			console.log("saved!");
			// pass data retrieved to parent controller
			$mdDialog.hide(response.data);	
		});			  
	  };
	  
	  // Autocomplete functions	  
	  $scope.coordinators = [];
	  $scope.collaborators = [];
	  $scope.locations = [];
	  $scope.searchText = null;
	  $scope.selectedItem = null;
	  
	  $http.get('/resource/project/list-members-not-me', {
			params: {
				idProject: $stateParams.idProject
			}
		}).then(function(response) {
		  $scope.members = response.data;
	  });
	  
	  //$http.get('/resource/lab/list').then(function(response) {
	  //		$scope.labs = response.data;
	  //});
	  
	  /**
	   * Return the proper object when the append is called.
	   */
	  function transformChip(chip) {
		  // If it is an object, it's already a known chip
		  if (angular.isObject(chip)) {
			  return chip;
      	  }
		  
		  // Otherwise, create a new one
	      return { name: chip, type: 'new' }
	  }
	    
	  $scope.querySearch = function querySearch(query) {		  
		  var results = query ? $scope.members.filter(createFilterFor(query)) : [];
		  return results;
	  }
	  
	  $scope.querySearch2 = function(query) {		  		  
		  
		  return $http.get('/resource/lab/find-by-term', {
			params: {
				term: query
			}
		  }).then(function(response) {
			  return response.data;
		  });
		  
	  }
	  
	  /**
	  * Create filter function for a query string
	  */
	  function createFilterFor(query) {
		  var lowercaseQuery = angular.lowercase(query);
		  return function filterFn(member) {
		    return (member.fullName.indexOf(lowercaseQuery) != -1);
		  };
	  }
	  
};

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
			avatarHeight: '@avatarH'
		},
		template: '<div class="generic-avatar" style="width: {{avatarWidth}}px; height: {{avatarHeight}}px;">' +
			'<div class="avatar-circle" style="background-color: {{genericAvatar.background}};"></div>' +
			'<span class="name">{{genericAvatar.initials}}</span>' +
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

// Project avatar
app.directive('projectAvatar', ["projectAvatarService", function (projectAvatarService) {
	var controller = function ($scope) {		
		$scope.$watch("mProject", function(newValue, oldValue, scope) {
			if (newValue) {
				$scope.imageAvailable = false;
				if (!$scope.mProject.avatar) {
					$scope.genericAvatar = projectAvatarService.getAvatar($scope.mProject);
				} else {
					$scope.imageAvailable = true;
				}	
			}
        });
	};
	return {
		restrict: 'E',
		scope: {
			mProject: '=project',
			avatarWidth: '@avatarW',
			avatarHeight: '@avatarH'
		},
		template: '<div class="generic-avatar" style="width: {{avatarWidth}}px; height: {{avatarHeight}}px;">' +
			'<div class="avatar-circle" style="background-color: {{genericAvatar.background}};"></div>' +
			'<span class="project-name">{{genericAvatar.name}}</span>' +
			'<div class="img-avatar" data-ng-if="imageAvailable" style="background-image:url({{mFabber.avatar}})"></div>' +
			'</div>',
		controller: controller
	};
}])
.factory("projectAvatarService", function(){
    var projectAvatarService = function(project) {
    	var colorCodes = ["#1abc9c", "#2ecc71", "#3498db", "#9b59b6", "#34495e", "#16a085", "#27ae60", 
    	                  "#2980b9", "#8e44ad", "#2c3e50", "#f1c40f", "#e67e22", "#e74c3c", "#95a5a6", 
    	                  "#f39c12", "#d35400", "#c0392b", "#bdc3c7", "#7f8c8d"];
      
		var i1 = "", i2 = "", nameArray = [];
		
		i1 = angular.uppercase(project.name.charAt(0));
		nameArray = project.name.split(" ");
		
		if (nameArray.length > 2) {
			i2 = angular.uppercase(nameArray[nameArray.length - 1].charAt(0));
		} else if(nameArray.length === 2) {
			i2 = angular.uppercase(nameArray[1].charAt(0));
		} else {
			i2 = angular.uppercase(nameArray[0].charAt(0));
		}
		
		var initials = i1 + "" + i2;
		var charIndex = initials.charCodeAt(0) - 48,
	    	colourIndex = charIndex % 19;
		
		var background = colorCodes[colourIndex];
		return ({ "name": project.name, "background": background });
    }
    return {
      getAvatar: projectAvatarService
    }
});
