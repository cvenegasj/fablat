<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />

<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700" type="text/css" />
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons" />
<link rel="stylesheet" href="https://code.getmdl.io/1.2.1/material.light_blue-green.min.css" />
<script defer src="https://code.getmdl.io/1.2.1/material.min.js"></script>

<!--<link rel="stylesheet" href="css/public.css" /> -->

<style>

.login-card .mdl-card {
	
}

.login-card .mdl-card__title {
	color: #fff;
	background-color: #0091EA;
	text-align: center;
	display: flex;
	flex-direction: column;
	align-items: center;
}

.login-card .mdl-card__title > img {
	height: 55px; 
	width: auto;
	margin: 0 auto 10px;
}

.centered {
	margin: 0 auto;
}

.centered-flex {
	display: flex; 
	flex-direction: column;
	align-items: center;
}

</style>
</head>

<body>
	
	<div class="mdl-grid">
	
	<div class="mdl-layout-spacer"></div>
	<div class="login-card mdl-card mdl-shadow--4dp mdl-cell mdl-cell--5-col mdl-cell--6-col-tablet mdl-cell--4-col-phone">
		<div class="mdl-card__title">
			<img src="https://i.imgur.com/6IQ6pH3.png" />
		
			<h2 class="mdl-card__title-text centered">Fab Lat Network</h2>
		</div>
		<div class="mdl-card__supporting-text">
			<#if RequestParameters['error']??>
				<div style="color: #D32F2F;">
					There was a problem logging in. Please try again.
				</div>
			</#if>
			
			<form role="form" action="login" method="post" class="centered-flex">
			  <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
			    <input class="mdl-textfield__input" type="text" id="username" name="username" />
			    <label class="mdl-textfield__label" for="username">Email</label>
			  </div>
			  
			  <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
			    <input class="mdl-textfield__input" type="password" id="password" name="password" />
			    <label class="mdl-textfield__label" for="password">Password</label>
			  </div>
			  
			  <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			  
			  <div style="margin-top: 15px; margin-bottom: 30px;">
			  	<div style="display: inline-block; padding: 10px;">
			  		<a href="http://fablat.net/public.html" style="color: #00C853; font-size: 14px;">Create an account</a>
			  	</div>
			  	|
			  	<div style="display: inline-block; padding: 10px;">
			  		<a href="http://fablat.net/public.html" style="color: #00C853; font-size: 14px;">Forgot your password?</a>
			  	</div>
			  </div>
			  
			  <div>
				  <button type="submit" class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect">
				  	Submit
				  </button>
			  </div>
			  
			</form>
		</div>
	</div>
	
	<div class="mdl-layout-spacer"></div>
	
	</div>
	
	<!-- <div class="container">
		<form role="form" action="login" method="post">
		  <div class="form-group">
		    <label for="username">Username:</label>
		    <input type="text" class="form-control" id="username" name="username"/>
		  </div>
		  <div class="form-group">
		    <label for="password">Password:</label>
		    <input type="password" class="form-control" id="password" name="password"/>
		  </div>
		  <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		  <button type="submit" class="btn btn-primary">Submit</button>
		</form>
		
		<a href="http://fablat.net/public.html">Do not have an account? Register here.</a>
	</div> -->

</body>
</html>