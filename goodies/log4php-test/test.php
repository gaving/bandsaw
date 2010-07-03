<?php

define(LOG4PHP_DIR, 'log4j/');

require_once(LOG4PHP_DIR . '/LoggerManager.php');

$logger =& LoggerManager::getLogger('Log4PHPTestLogger');

foreach($argv as $arg) {
    $logger->debug("This is a debug for $arg!");
    $logger->warn("This is a warning for $arg");
    $logger->info("This is a info for $arg");
    $logger->error("This is an error for $arg");
    $logger->fatal("This is a fatal for $arg");
}

?>
