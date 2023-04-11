<?php
	session_start();
	include ("top.php");

	$cartGet = "SELECT name, bname, colour, price, count
	FROM wines
	JOIN colours ON wines.colourid = colours.idcolours
	JOIN breweries ON wines.idbrewery = breweries.idbreweries
	JOIN cart ON cart.idprofile = '$_SESSION[LoginData]'
	AND wines.idwines = cart.idwine;";

	$result = mysqli_query($conn, $cartGet);

	while ($row = mysqli_fetch_assoc($result))
	{
	$total = $row['count']*$row['price'];
	$sql2 = "INSERT INTO purchases (wine, amount, price, total, idperson) VALUES
	('$row[name]', '$row[count]', '$row[price]', '$total', '$_SESSION[LoginData]')";
	mysqli_query($conn, $sql2);
	}

	$remove = "DELETE FROM cart WHERE idprofile = '$_SESSION[LoginData]'";
	mysqli_query($conn, $remove);
?>

<div id="mainContainer" class="center">
	<h1> Thank you for your purchase </h1>
</div>

<?php
	include ("bottom.php");
?>