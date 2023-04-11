<?php
	session_start();
	include ("top.php");

	$sql0 = "select idprofiles from profiles where logged=1";
	$source0 = mysqli_query($conn, $sql0);
	if(mysqli_num_rows($source0) == 0)
	{
	}
	else 
	{
		$_SESSION['LoginData'] = mysqli_fetch_array($source0)['idprofiles'];
	}
?>

<div id="mainContainer" class="center">
	<div class="textItem">
	<h1> Welcome to our website </h1>
	<h3> Here we sell wines from all over the world </h3>
	<img class="welcm" src="Welcome.png"> </img>
	<h3> Should you wish to see all we have on offer just hit the search button </h3>
	
	</div>
</div>

<?php
	include ("bottom.php");
?>