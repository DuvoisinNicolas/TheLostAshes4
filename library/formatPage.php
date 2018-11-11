<?php

function startPage() {
    echo '<!DOCTYPE html>
          <html lang="fr">
                <head>
                <meta charset="utf-8">
                <link rel="stylesheet" type="text/css" href="/css/style.css">
                <title>The Lost Ashes 4</title>
                </head>
                <body>
                    <nav id="menu">
                     <ul>';
    if ($_SESSION['connected']) {
        echo '<li><a href = "default.asp">Menu</a ></li>
                          <li><a href = "news.asp" >Inventaire</a></li>
                          <li><a href = "contact.asp" >Règles</a></li>
                          <li><a href = "contact.asp" >Compte</a></li>
                          <li style = "float:right" ><a href = "about.asp" >Déconnexion</a ></li >
                ';
    } else {
        echo '<li><a href = "default.asp">Menu</a ></li>
                          <li><a href = "news.asp" >Règles</a></li>
                          <li><a href = "contact.asp" >Inscription</a></li>
                          <li><a href = "about.asp" >Connexion</a ></li >
                ';
    }
    echo '</ul> 
          </nav>';
}

function endPage() {
    echo '    </body>
</html>';
}