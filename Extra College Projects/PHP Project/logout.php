<?php
	session_start();

  $conn=mysqli_connect("localhost", "root", "");
  mysqli_select_db($conn, "wine");

	$sql = "update profiles set logged = 0 where logged = 1";
	$source = mysqli_query($conn, $sql);

header('Location: profile.php');
?>