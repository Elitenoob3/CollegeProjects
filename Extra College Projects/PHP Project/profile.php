<?php
	session_start();
	include ("top.php");

	$sql = "select name from profiles where logged=1";
	$source = mysqli_query($conn, $sql);

	if(mysqli_num_rows($source) == 0)
	{
	header('Location: login.php');
	}
	else 
	{
		$sql2 = "select idprofiles from profiles where logged=1";
		$source2 = mysqli_query($conn, $sql2);
		$_SESSION['LoginData'] = mysqli_fetch_array($source2)['idprofiles'];
	}
?>

<div id="mainContainer">
	<div class="center" id = "LoginArea">
	<?php
	$row = mysqli_fetch_array($source);
	print "<h1> Welcome $row[name] </h1>"
	?>
	<h3> If you wish to log out </h3>
	<a class="textItem" href="logout.php"> Press this button </a>

	<h3> Should you wish to change your address input it below </h3>
	<form class="VertCol" action="updated.php" method="get">
			<input type="text" name="UpdateAdress" class="Field" placeholder="New Address" width="400" required>
			<input type="submit" class="Button" value="Update" >
	</form>
	</div>
</div>

<?php
	include ("bottom.php");
?>