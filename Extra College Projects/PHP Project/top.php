<?php
  $conn=mysqli_connect("localhost", "root", "");
  mysqli_select_db($conn, "wine");
?>

<html>

<head>
	<title></title>
	<link rel="stylesheet" href="style.css">
</head>

<body>
	<div class="flexbox" id="topBanner">
		<div class="flexbox">
			<a href="index.php">
				<img src="Icons/Home.png" class="icon">
			</a>
			<form action="search.php" method="GET">
				<input type="text" placeholder="Search from here" name="searchTerm" width="200" id="searchBox">
				<input type="submit" value="Search" id="searchButton">
			</form>
			<form action="admin.php">
				<input type="submit" value="Admin" id="searchButton">
			</form>
		</div>
		<div>
			<a href="profile.php">
				<img src="Icons/Profile.png" class="icon">
			</a>
			<a href="basket.php">
				<img src="Icons/Cart.png" class="icon">
			</a>
		</div>
	</div>

	<div class="flexbox" id="globalContainer">
		<div class="flexbox" id="leftMenu">

		<p class="boxItem"> Pick a brewery </p>

		<?php
		  $sql = " SELECT * FROM breweries ORDER BY bname ASC ";
		  $source = mysqli_query($conn, $sql);
		  while($row=mysqli_fetch_array($source))
		  {
			print '<a href="breweries.php?idbreweries='.$row['idbreweries'].'" class="boxItem" >'.$row['bname'].' </a> <br>' ;
		  }
		?>
		</div>