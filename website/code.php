<?php 
class MyZipArchive extends ZipArchive{
  public function addDirectory($dir, $base = 0) {
    foreach(glob($dir . '/*') as $file) {
      if(is_dir($file))
        $this->addDirectory($file, $base);
      else
        $this->addFile($file, substr($file, $base));
    }
  }
}

function getAllFilesRecursive($dir, &$files) {
  foreach(glob($dir."/*") as $file) {
    if(is_dir($file))
      getAllFilesRecursive($file, $files);
    else
      $files[] = $file;
  }
}

if (isset($_POST["download"])) {
  $path = "D:\\Users\\Programmering\\Workspace";
  getAllFilesRecursive($path."\\javachat\\src", $files);
  $mod_times = array_map("filemtime", $files);
  arsort($mod_times);

  if (!file_exists('javachat.zip') || array_shift($mod_times) > filemtime('javachat.zip')) {
    $zip = new MyZipArchive;
    $zip->open('javachat.zip', MyZipArchive::OVERWRITE);
    $zip->addDirectory($path."\\javachat\\src", strlen($path) + 1);
    $zip->addFile($path."\\javachat\\.classpath", "javachat\\.classpath");
    $zip->addFile($path."\\javachat\\.project", "javachat\\.project");
    $zip->close();
    header("Location: javachat.zip");
  }
  
} else { ?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Javachat - About</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <link href="bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
    <link href="main.css" rel="stylesheet">
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
                <li class="active"><a href="/code.php">Koden</a></li>
                <!-- Read about Bootstrap dropdowns at http://twitter.github.com/bootstrap/javascript.html#dropdowns -->
                <li class="dropdown">
                  <a href="#" class="dropdown-toggle" data-toggle="dropdown">How to <b class="caret"></b></a>
                  <ul class="dropdown-menu">
                    <li class="nav-header">Server</li>
                    <li><a href="/#feature1">Protokollet</a></li>
                    <li class="divider"></li>
                    <li class="nav-header">Client</li>
                    <li><a href="/#feature2">Huvud-klienten</a></li>
                    <li><a href="/#feature3">Privat chattrum</a></li>
                    <li><a href="/#feature4">Fil-överföring</a></li>
                    <li><a href="/#feature5">Fil-mottagning</a></li>
                  </ul>
                </li>
              </ul>
            </div><!-- /.nav-collapse -->
      	</div><!-- /.container -->
      </div><!-- /.navbar-inner -->
    </div><!-- /.navbar -->

    <!-- Marketing messaging and featurettes
    ================================================== -->
    <!-- Wrap the rest of the page in another container to center all the content. -->

    <div class="container marketing">

      <div class="page-header">
        <h1>Koden</h1>
      </div>
      <form method="post">
        <button name="download">Download here</button>
      </form>
      
    </div><!-- /.container -->

    <!-- FOOTER -->
    <footer>
      <div class="container">
        <p class="pull-right"><a href="#">Back to top</a></p>
        <p>© 2013 Nätverksprogrammering, LTH · <a href="about.php">Contact</a></p>
      </div>
    </footer>
  </div><!-- /#site-wrapper -->


  <!-- Le javascript
  ================================================== -->
  <!-- Placed at the end of the document so the pages load faster -->
  <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
	<script src="bootstrap/js/bootstrap.min.js"></script>

</body>
</html>
<?php } ?>
