<?php

    date_default_timezone_set('Europe/Berlin');

    require_once('log4php/src/main/php/Logger.php');
    Logger::configure('log4php.properties');

    $logger = Logger::getRootLogger();
    // $logger = Logger::getLogger("Log4PHPTestLogger");

    $message = (!isset($argv[1])) ? md5(rand(0,10)) : implode(' ', array_slice($argv, 1));
    $logger->info($message);
    $logger->warn($message);
    $logger->debug($message);
    $logger->debug("select * from masterqueue limit 10");

    try {
        throw new Exception("yeah");
    } catch (Exception $e) {
        $logger->error("hoho", $e);
    }

?>
