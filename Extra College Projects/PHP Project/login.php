<?php
	include ("top.php");
?>

<div id="mainContainer">
	<div class="center" id = "LoginArea">
	<h1> Welcome to the log in page </h1>
	
		<div class=textItem>
		<h3> Please enter your login credidentials below </h3>
		
		<form action="loginlogic.php" class="VertCol" method="get">
			<label for="email">Email or Username:</label>
			<input class="Field" type="text" id="email" name="email"><br>

			<label for="password">Password:</label>
			<input class="Field" type="password" id="password" name="password"><br><br>

			<input class="Button" type="submit" value="Log In">
		</form>
		</div>
		<h1> or </h1>
		<a class="textItem" href="newProfile.php"> Make a new account </a>

	</div>
</div>

<?php
	include ("bottom.php");
?>