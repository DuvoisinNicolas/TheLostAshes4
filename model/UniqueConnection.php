<?php
class UniqueConnection {

    const HOST = 'mysql-thelostashes4.alwaysdata.net';
    const IDENTIFIANT = '170447';
    const MDP = 'tla';
    const BD = 'thelostashes4_db';

    private $link;
    private static $instance;

    private function __construct() {
        $this->link = mysqli_connect(UniqueConnection::HOST,UniqueConnection::IDENTIFIANT,UniqueConnection::MDP)
        or die ('Problème de connexion au serveur :' . mysqli_connect_error());
        mysqli_select_db($this->link,UniqueConnection::BD)
        or die ('Problème de sélection model : ' . mysqli_error($this->link));
    }
    public function getConnexion () {
        return $this->link;
    }
    static public function getInstance () {
        if (null == self::$instance)
            self::$instance = new UniqueConnection();
        return self::$instance;
    }
}