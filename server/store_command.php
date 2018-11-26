<?php

require 'conf.php';

$contents = file_get_contents('php://input');

$json = json_decode($contents, true);

$list = array();

foreach ($json as $key => $value) {
	$item = R::dispense('eegvalue');

	$item->eeg1 = $value["eeg1"];
	$item->eeg2 = $value["eeg2"];
	$item->eeg3 = $value["eeg3"];
	$item->eeg4 = $value["eeg4"];
	$item->command = $value["command"];

	array_push($list, $item);
}

R::storeAll($list);

?>