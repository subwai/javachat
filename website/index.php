<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Javachat - Home</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <link href="bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
    <link href="main.css" rel="stylesheet">

  	<script src="http://ajax.googleapis.com/ajax/libs/webfont/1/webfont.js" type="text/javascript" async=""></script>
</head>

<body screen_capture_injected="true">
	<div id="site-wrapper">
    <!-- NAVBAR
    ================================================== -->

    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
      	<div class="container">
            <!-- Responsive Navbar Part 1: Button for triggering responsive navbar (not covered in tutorial). Include responsive CSS to utilize. -->
            <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
            </button>
            <a class="brand" href="/">Javachat</a>
            <!-- Responsive Navbar Part 2: Place all navbar contents you want collapsed withing .navbar-collapse.collapse. -->
            <div class="nav-collapse collapse">
              <ul class="nav">
                <li><a href="/about.php">About us</a></li>
                <!-- Read about Bootstrap dropdowns at http://twitter.github.com/bootstrap/javascript.html#dropdowns -->
                <li class="dropdown">
                  <a href="#" class="dropdown-toggle" data-toggle="dropdown">How to <b class="caret"></b></a>
                  <ul class="dropdown-menu">
                    <li class="nav-header">Server</li>
                    <li><a href="/#feature1">Explenation#1</a></li>
                    <li class="divider"></li>
                    <li class="nav-header">Client</li>
                    <li><a href="/#feature2">Explenation#2</a></li>
                    <li><a href="/#feature3">Explenation#3</a></li>
                    <li><a href="/#feature4">Explenation#4</a></li>
                    <li><a href="/#feature5">Explenation#5</a></li>
                  </ul>
                </li>
              </ul>
            </div><!--/.nav-collapse -->
      	</div><!-- /.container -->
      </div><!-- /.navbar-inner -->
    </div><!-- /.navbar -->

    <!-- Marketing messaging and featurettes
    ================================================== -->
    <!-- Wrap the rest of the page in another container to center all the content. -->

    <div class="container marketing">

      <!-- Three columns of text below the carousel -->
      <div class="row">
        <div class="span4">
          <img style="width: 140px; height: 140px;" src="/pics/threads.png">
          <h2>Trådning</h2>
          <p>Varje klienten får sin egen tråd på servern som svarar på allt klienten frågar efter.</p>
          <p><a class="btn" href="javascript:showDetails('threads');">View details »</a></p>
        </div><!-- /.span4 -->
        <div class="span4">
          <img style="width: 140px; height: 140px;" src="/pics/protocol.png">
          <h2>Protokoll</h2>
          <p>Vårt chatprogram konverserar genom vårt egna protokoll som finns i <code>shared/ChatProtocol.java</code>.</p>
          <p><a class="btn" href="javascript:showDetails('protocol');">View details »</a></p>
        </div><!-- /.span4 -->
        <div class="span4">
          <img style="width: 140px; height: 140px;" src="/pics/connection.png">
          <h2>Anslutning</h2>
          <p>Anslutningen sker genom en enkel TCP socket, utan någon form av kryptering.</p>
          <p><a class="btn" href="javascript:showDetails('connection');">View details »</a></p>
        </div><!-- /.span4 -->
      </div><!-- /.row -->
      <div id="details-wrap">
      	<div id="threads-d" hidden>
      		<hr>
      		<h2>Trådning</h2>
      		<p>Vi andvänder trådar... Varje klienten får sin egen tråd på servern som svarar på allt klienten frågar efter. Klienten har en nästan lika dan tråd som lystnar på allt servern svarar med. Vid fil överföring startas individuella trådar hos de två involverade användarna. Dessa trådars uppgift är endast att skicka / ta emot en fil, sedan stängs de av.</p>
      	</div>
      	<div id="protocol-d" hidden>
      		<hr>
      		<h2>Protokoll</h2>
      		<p>Vårt chatprogram konverserar genom vårt egna protokoll som finns i <code>shared/ChatProtocol.java</code>. Denna fil är en enum med alla olika interaktioner man skulle kunna göra mellan klient/server. Varje gång man klient pratar med servern eller vice versa, så börjar meddelandet med ett av protokollets typer, förljt av alla argument som kan tänka behövas. Argumenten är skillt med mellanrum, dock med specialfall för citationstecken då man måste kunna skriva mellanslag i chatmeddelanden.</p>
      	</div>
      	<div id="connection-d" hidden>
      		<hr>
      		<h2>Anslutning</h2>
      		<p>Anslutningen sker genom en enkel TCP socket, utan någon form av kryptering. Anslutningen skapas först då användaren trycker på <b>Login</b>.</p>
      	</div>
      </div>


      <!-- START THE FEATURETTES -->
      <div class="hero-unit">
        <h1>Server</h1>
        <div class="featurette" id="feature1">
          <img class="featurette-image pull-right" src="/pics/server.png">
          <h2 class="featurette-heading">And lastly, this one. <span class="muted">Checkmate.</span></h2>
          <p class="lead">Donec ullamcorper nulla non metus auctor fringilla. Vestibulum id ligula porta felis euismod semper. Praesent commodo cursus magna, vel scelerisque nisl consectetur. Fusce dapibus, tellus ac cursus commodo.</p>
        </div>
      </div>

			<div class="hero-unit">
        <h1>Client</h1>

		  	<div class="featurette" id="feature2">
	        <img class="featurette-image pull-right" src="/pics/chat_client_1.png">
	        <h2 class="featurette-heading">First featurette headling.<br><span class="muted">It'll blow your mind.</span></h2>
	        <p class="lead">Donec ullamcorper nulla non metus auctor fringilla. Vestibulum id ligula porta felis euismod semper. Praesent commodo cursus magna, vel scelerisque nisl consectetur. Fusce dapibus, tellus ac cursus commodo.</p>
	        <img class="featurette-image pull-left" src="/pics/chat_client_2.png">
	        <p class="lead">Donec ullamcorper nulla non metus auctor fringilla. Vestibulum id ligula porta felis euismod semper. Praesent commodo cursus magna, vel scelerisque nisl consectetur. Fusce dapibus, tellus ac cursus commodo.</p>
	        <img class="featurette-image pull-right" src="/pics/chat_client_admin.png">
	        <p class="lead">Donec ullamcorper nulla non metus auctor fringilla. Vestibulum id ligula porta felis euismod semper. Praesent commodo cursus magna, vel scelerisque nisl consectetur. Fusce dapibus, tellus ac cursus commodo.</p>
	      </div>

	      <hr class="featurette-divider">

	      <div class="featurette" id="feature3">
	        <img class="featurette-image pull-left" src="/pics/priv_chat.png">
	        <h2 class="featurette-heading">Oh yeah, it's that good. <span class="muted">See for yourself.</span></h2>
	        <p class="lead">Donec ullamcorper nulla non metus auctor fringilla. Vestibulum id ligula porta felis euismod semper. Praesent commodo cursus magna, vel scelerisque nisl consectetur. Fusce dapibus, tellus ac cursus commodo.</p>
	      </div>

	      <hr class="featurette-divider">

	      <div class="featurette" id="feature4">
	        <img class="featurette-image pull-right" src="/pics/file_transfer_select.png">	        
	        <h2 class="featurette-heading">Oh yeah, it's that good. <span class="muted">See for yourself.</span></h2>
	        <p class="lead clearfix">Donec ullamcorper nulla non metus auctor fringilla. Vestibulum id ligula porta felis euismod semper. Praesent commodo cursus magna, vel scelerisque nisl consectetur. Fusce dapibus, tellus ac cursus commodo.</p>
	        <img class="featurette-image pull-right" src="/pics/file_transfer_send.png">
	        <p class="lead clearfix">Donec ullamcorper nulla non metus auctor fringilla. Vestibulum id ligula porta felis euismod semper. Praesent commodo cursus magna, vel scelerisque nisl consectetur. Fusce dapibus, tellus ac cursus commodo.</p>
	        
	      </div>

	      <hr class="featurette-divider">

	      <div class="featurette" id="feature5">
	        <img class="featurette-image pull-left" src="/pics/file_recieve.png">
	        <h2 class="featurette-heading">Oh yeah, it's that good. <span class="muted">See for yourself.</span></h2>
	        <p class="lead">Donec ullamcorper nulla non metus auctor fringilla. Vestibulum id ligula porta felis euismod semper. Praesent commodo cursus magna, vel scelerisque nisl consectetur. Fusce dapibus, tellus ac cursus commodo.</p>
	      </div>
			</div>

      <!-- /END THE FEATURETTES -->

    </div><!-- /.container -->

    <!-- FOOTER -->
    <footer>
      <div class="container">
        <p class="pull-right"><a href="#">Back to top</a></p>
        <p>© 2013 Nätverksprogrammering, LTH · <a href="about.php">Contact</a></p>
      </div>
    </footer>

  </div>

  <!-- Le javascript
  ================================================== -->
  <!-- Placed at the end of the document so the pages load faster -->
  <script src="http://code.jquery.com/jquery.js"></script>
	<script src="bootstrap/js/bootstrap.min.js"></script>
	<script>
		var current;
		function showDetails(val) {
			if (current != val) {
				$("#details-wrap div").hide("fast");
				$("#"+val+"-d").show("fast");
				current = val;
			}
		}
	</script>
</body>
</html>