<?php

define(LOG4PHP_DIR, 'log4j/');

require_once(LOG4PHP_DIR . '/LoggerManager.php');

$logger =& LoggerManager::getLogger('Log4PHPTestLogger');

$logger->debug("This is a debug for $argv[0]!");
$logger->warn("This is a warning for $argv[0]");
$logger->info("This is a info for $argv[0]");
$logger->error("This is an error for $argv[0]");
$logger->fatal("This is a fatal for $argv[0]");

$logger->info(implode(' ', $argv));

?>
