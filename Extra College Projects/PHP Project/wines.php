<?php
	session_start();
	include ("top.php");
?>

<div id="mainContainer">
	
	<?php
	$idwines=$_GET['idwines'];
	$sql="SELECT name, bname, colour, price 
	FROM wines
	JOIN colours ON wines.colourid = colours.idcolours
	JOIN breweries ON wines.idbrewery = breweries.idbreweries
	WHERE idwines = '$idwines'";
	$resource = mysqli_query($conn, $sql);
	$row = mysqli_fetch_array($resource);

	$_SESSION['wine_id'] = $idwines;
	?>	



	<div class='center'>

		<?php
		$wimg = "WineImages/".$idwines.".jpeg";

			print "<div class='textItem'>";

		if (file_exists($wimg))
		{	
			$wimg="WineImages/".$idwines.".jpeg";
			print '<img src="'.$wimg.'" class="winebottle"  style="max-height: 400px; max-width: 400px"   ;><br>';
		}
		else
		{
			print '<div class="winebottle"> No Image found</div>';
		}

			print '</div>';
		?>

		<div class='textItem'>

		<?=$row['name']?>
		Made by:
		<?=$row['bname']?>
		<br>
		This is a <?=$row['colour']?> wine.
		<br>
		This wine costs <?=$row['price']?> $.
		<br>
		Press the button below to add to cart.
		<br>
		Note that you need to make an account <br>
		in order to make purchases. <br>

		Please input the ammount you would like to purchase

		<form action="cartlogic.php" class="Center" method="get">
			<input type="number" name="bottles" min="1" max="10" class="scheckbox" value="1" required>
			<input type="submit" class="addCart" value="Purchase">
		</form>

		</div>

	</div>;
</div>

<?php
	include ("bottom.php");
?>