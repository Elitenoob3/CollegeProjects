<?php
session_start();

  $conn=mysqli_connect("localhost", "root", "");
  mysqli_select_db($conn, "wine");

  if(isset($_POST['wine']))
  {
  $winename = $_POST['wine'];
  $idbrewery = $_POST['breweries'];
  $colourid = $_POST['colours'];
  $price = $_POST['price'];

  $sqli = "Insert into wines (name, idbrewery, colourid, price) values
	('$winename', $idbrewery, $colourid, $price)";
	mysqli_query($conn, $sqli);
  }

  if(isset($_POST['bame']))
  {
  $bname = $_POST['bame'];

  $sqli = "Insert into breweries (bname) value ('$bname')";
  mysqli_query($conn, $sqli);
  }
?>

<html>

<head>
	<title></title>
	<link rel="stylesheet" href="style.css">
</head>

<div class="VertCol" id="LoginArea">

<form action="admin.php" class="VertCol" method="post">
			<label >Wine</label>
			<input class="Field" type="text" name="wine" required><br>

			<label >Colour:</label>
			<select name="colours" id="colours">
			  <option value="1">Red</option>
			  <option value="2">White</option>
			  <option value="3">Rose</option>
			</select>
			<br>

			<label for="breweries"> Wineries/Breweries: </label>
			<select name="breweries">
			<?php
			$sql = "select * from breweries";
			$res = mysqli_query($conn, $sql);

			while ($row = mysqli_fetch_assoc($res))
			{
			print '<option value="';echo $row['idbreweries'];print '">'; echo $row['bname']; print '</option>';
			}
			?>
			</select>
			<br>

			<label for="price"> Price: </label>
			<input class="Field" type="text" name="price" required><br>

			<input class="Button" type="submit" value="Add wine to store">
</form>

	<h3> Or add your Brewery to the list first<h3>


	
<form action="admin.php" class="VertCol" method="post">
		<input class="Field" type="text" name="bame" required>
		<input class="Button" type="submit" value="Add">
</form>

<a href="index.php">
	<img src="Icons/Home.png" class="icon">
</a>

</div>



</html>