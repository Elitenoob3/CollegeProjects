<?php
session_start();

  $conn=mysqli_connect("localhost", "root", "");
  mysqli_select_db($conn, "wine");

$name = $_GET['name'];
$email = $_GET['email'];
$password = $_GET['password'];
$adress = $_GET['adress'];

$sql = "insert into profiles (name, email, pass, adress, logged) values ('$name','$email','$password', '$adress' ,0)";
mysqli_query($conn, $sql);

header('Location: login.php');
?>