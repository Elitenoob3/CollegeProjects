<?php
	session_start();
	include ("top.php");

	$LoginData = $_SESSION['LoginData'];
	$newAdress = $_GET['UpdateAdress'];
	$sql = "update profiles set adress = '$newAdress' where logged = 1";
	mysqli_query($conn, $sql);
?>

<div id="mainContainer" class="center">
	<h1> Adress updated successfully </h1>
</div>

<?php
	include ("bottom.php");
?>