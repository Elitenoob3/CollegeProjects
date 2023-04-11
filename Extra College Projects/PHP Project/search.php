<?php
	session_start();
	include ("top.php");
	$searchTerm = $_GET['searchTerm'];
?>

<div id="mainContainer">

	<div class="VertCol">
	<div class="category"> <p class="categoryText"> Breweries </p> </div>

		<?php

			$sql = "SELECT idbreweries, bname FROM breweries WHERE bname LIKE '%".$searchTerm."%'";
			$resource = mysqli_query($conn, $sql);

			if(mysqli_num_rows($resource) == 0) 
			{
				print "<div class='centerP'>";
				print "<i class='textItem'>No results</i>";
				print "</div>";
			}

			print "<div class='center'>";

			while($row = mysqli_fetch_array($resource)) 
			{
				
				$replace = str_replace ($searchTerm,"<b>$searchTerm</b>", $row['bname']);
				print '<a href="breweries.php?idbreweries='.$row['idbreweries'].'" class="textItem">'.$replace.'</a>';
				
			}

			print "</div>";

		?>
	</div>

	<div class="VertCol">
	<div class="category"> <p class="categoryText"> Wines </p> </div>
		<?php

			$sql = "SELECT idwines, name FROM wines WHERE name LIKE '%".$searchTerm."%'";
			$resource = mysqli_query($conn, $sql);

			if(mysqli_num_rows($resource) == 0) 
			{
				print "<div class='centerP'>";
				print "<i class='textItem'>No results</i>";
				print "</div>";
			}

				print "<div class='gridDisplay'>";

			while($row = mysqli_fetch_array($resource)) 
			{
				$replace = str_replace ($searchTerm,"<b>$searchTerm</b>", $row['name']);
				print '<a href="wines.php?idwines='.$row['idwines'].'" class="textItem"> <img class="winebottle" src="WineImages/'.$row['idwines'].'.jpeg" alt="image of wine"> <div class="center">'.$replace.'</div> </a>';
			}

				print "</div>";
		?>
	</div>

</div>

<?php
	include ("bottom.php");
?>