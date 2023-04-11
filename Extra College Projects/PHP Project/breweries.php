<?php
	session_start();
	include ("top.php");
?>

<?php
	$idbreweries=$_GET['idbreweries'];
	$sqlBName = "Select bname from breweries where idbreweries =".$idbreweries;
	$sourceN = mysqli_query($conn, $sqlBName);
	$row = mysqli_fetch_array($sourceN);
?>
	
<div id="mainContainer">
	<div  class="gridDisplay">
		<?php
		$sql = "select idwines, name, bname, colour, price
		from wines, colours, breweries
		where wines.colourid = colours.idcolours and
		wines.idbrewery = breweries.idbreweries and
		wines.idbrewery = ".$idbreweries;
		$source = mysqli_query($conn, $sql);

		while($row = mysqli_fetch_array($source))
		{
			print '<a href="wines.php?idwines='.$row['idwines'].'" class="textItem"> <img class="winebottle" src="WineImages/'.$row['idwines'].'.jpeg" alt="image of wine"> <div class="center">'.$row['bname'].'</div> </a>';
		}

		?>
	</div>
</div>

<?php
	include ("bottom.php");
?>