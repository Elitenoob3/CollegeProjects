<?php
	session_start();
	include ("top.php");
?>

<div id="mainContainer">
	<div class="center" id = "LoginArea">
		<h1> Create a new profile </h1>
	
		<div class=textItem>
		<h3> Please enter your profile details below </h3>

		<form action="datainsert.php" class="VertCol" method="get">
			<label for="email">Email:</label>
			<input class="Field" type="text" id="email" name="email" required><br>

			<label for="password">Password:</label>
			<input class="Field" type="password" id="password" name="password" required><br><br>

			<label for="name">Name:</label>
			<input class="Field" type="name" id="name" name="name" required><br><br>

			<label for="name">Shipping Adress:</label>
			<input class="Field" type="adress" id="adress" name="adress" required><br><br>

			<label for="agreement">I agree to the terms and conditions <br>
			and hereby declare that I am over 18 years of age</label>
			<input class="checkbox" type="checkbox" id="agree" required>

			<input class="Button" type="submit" value="Create Account">
		</form>
		</div>
	</div>
</div>

<?php
	include ("bottom.php");
?>