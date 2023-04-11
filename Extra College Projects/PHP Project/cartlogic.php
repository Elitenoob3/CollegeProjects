<?php
	session_start();

$conn=mysqli_connect("localhost", "root", "");
mysqli_select_db($conn, "wine");
	
$count = $_GET['bottles'];
$bottle = $_SESSION['wine_id'];
$LoginData = $_SESSION['LoginData'];

$sql = "insert into cart (idprofile, idwine, count) values ('$LoginData','$bottle','$count')";
mysqli_query($conn, $sql);

header('Location: basket.php');
?>