<?php
	session_start();
	include ("top.php");

	$sql = "select idprofiles from profiles where logged=1";
	$source = mysqli_query($conn, $sql);

	if(mysqli_num_rows($source) == 0)
	{
	header('Location: login.php');
	}
	else 
	{

	if (isset($_POST['submit'])) 
	{
		$id = $_POST['id'];
		$sql3 = "delete from cart where idcart = $id";
		mysqli_query($conn, $sql3);
	}

	$sql2 = "select idcart, name, bname, colour, price, count 
	from wines
	join colours on wines.colourid = colours.idcolours
	join breweries on wines.idbrewery = breweries.idbreweries
	join cart on cart.idprofile = $_SESSION[LoginData] and
	wines.idwines = cart.idwine;";

	$result = mysqli_query($conn, $sql2);

	if(mysqli_num_rows($result) != 0) $total = 0;
	}

	
?>


<div id="mainContainer">
	<div class="VertCol" id="LoginArea">
		<h1> These are the items that you have selected </h1>
		<h3> You may remove any of them if you wish to do so </h3>
		
			<table>
				<tr>
					<th> Name </th>
					<th> Winery/Brewery </th>
					<th> Wine Colour </th>
					<th> Price per unit </th>
					<th> Unit Count </th>
					<th> Remove </th>
				</tr>
				<?php while ($row = mysqli_fetch_assoc($result)) { 
				$total = $total + $row['price']*$row['count'];	
				?>
					<tr>
					  <td><?php echo $row['name']; ?></td>
					  <td><?php echo $row['bname']; ?></td>
					  <td><?php echo $row['colour']; ?></td>
					  <td><?php echo $row['price']; ?></td>
					  <td><?php echo $row['count']; ?></td>

					  <td>
					  <form method="post" action="">
						<input type="hidden" name="id" value="<?php echo $row['idcart']; ?>">
						<input type="submit" class="Button" name="submit" value="Remove">
					  </form>
					  </td>

					</tr>
				<?php } ?>
			</table>

			<?php
			
			if(mysqli_num_rows($result) != 0)
			{
			print '<h3> The total price of your purchase is ';
			echo $total;
			print'</h3>';
			print '<form method="get" action="purchase.php">
			<input type="submit" class="Button" style="margin: 20px" value="Purchase">
			</form>';
			}
			?>
	</div>
</div>

<?php
	include ("bottom.php");
?>