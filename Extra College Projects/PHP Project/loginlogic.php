<?php
  $conn=mysqli_connect("localhost", "root", "");
  mysqli_select_db($conn, "wine");

$email = $_GET['email'];
$password = $_GET['password'];

if(!empty($email) && !empty($password))
{
	$sql = "select * from profiles where email='$email' and pass=$password";

	$source = mysqli_query($conn, $sql);
	if(!empty($source))
	{
		$sql = "update profiles set logged = 1 where email='$email' and pass=$password";
		mysqli_query($conn, $sql);
	}

	header('Location: profile.php');
}
?>