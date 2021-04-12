-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 06 Gru 2020, 16:58
-- Wersja serwera: 10.4.14-MariaDB
-- Wersja PHP: 7.4.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `hotel`
--

DELIMITER $$
--
-- Procedury
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `AddGuestAndReserveTheRoom` (IN `na` VARCHAR(30), IN `sur` VARCHAR(30), IN `t` INT(1), IN `checkIn` DATE, IN `checkOut` DATE, OUT `result` INT(1))  NO SQL
BEGIN
INSERT INTO `guest`( `name`, `surname`) VALUES (na,sur);
SET @id =  LAST_INSERT_ID();

CALL Reservation(t,checkIn,checkOut,result,@id);

if (result = 1)
THEN
DELETE FROM `guest` WHERE `id`=@id;
END IF;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `RemoveGuestWhoCheckedOutMinimumWeekAgo` ()  NO SQL
BEGIN
DELETE `guest`,`booking` FROM `guest`,`booking` WHERE guest.id=booking.guestId AND booking.hasCkeckedOut =1 AND CURRENT_DATE > DATE_ADD(booking.timeOfCheckOut,INTERVAL 7 DAY);

DELETE guest.* FROM guest LEFT JOIN booking ON booking.guestId=guest.id WHERE  booking.id IS null;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `Reservation` (IN `t` INT(1) UNSIGNED, IN `checkIn` DATE, IN `checkOut` DATE, OUT `result` INT(1), IN `gID` INT UNSIGNED)  NO SQL
BEGIN
SET @roomId = (SELECT  room.id FROM room  WHERE room.type = t and room.id NOT IN(
    SELECT room.id FROM room  JOIN booking ON           room.id=booking.roomId WHERE                        booking.hasCkeckedOut=0  and 				   checkIn<=booking.endDate and    				   checkout>=booking.startDate )
  LIMIT 1);
IF (@roomId IS NULL) 
THEN 
 Set result=1;
ELSE 
INSERT INTO `booking`( `roomId`, `guestId`, `startDate`, `endDate` ) VALUES (@roomId,gID,checkin,checkout);
	Set result=0;
END IF;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateUrgencyInCleaning` ()  NO SQL
Begin
UPDATE `cleaning` SET `urgency` = 1 WHERE roomId IN(SELECT cleaning.roomId FROM cleaning JOIN booking ON cleaning.roomId=booking.roomId WHERE cleaning.status=3 and booking.hasCkeckedOut=0 and CURRENT_DATE=booking.startDate );

UPDATE `cleaning` SET `urgency` = 2 WHERE roomId IN(SELECT cleaning.roomId FROM cleaning JOIN booking ON cleaning.roomId=booking.roomId WHERE cleaning.status=3 and booking.hasCkeckedOut=0 and DATE_ADD(CURRENT_DATE, INTERVAL 1 DAY)= booking.startDate  );
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `booking`
--

CREATE TABLE `booking` (
  `id` int(11) UNSIGNED NOT NULL,
  `roomId` int(11) UNSIGNED NOT NULL,
  `guestId` int(11) UNSIGNED NOT NULL,
  `startDate` date NOT NULL,
  `endDate` date NOT NULL,
  `hasCheckedIn` bit(1) NOT NULL DEFAULT b'0',
  `timeOfCheckOut` datetime DEFAULT NULL ON UPDATE current_timestamp(),
  `hasCkeckedOut` bit(1) NOT NULL DEFAULT b'0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `cleaning`
--

CREATE TABLE `cleaning` (
  `id` int(11) UNSIGNED NOT NULL,
  `employeeId` int(11) UNSIGNED NOT NULL,
  `roomId` int(11) UNSIGNED NOT NULL,
  `urgency` enum('hight','middle','normal') NOT NULL DEFAULT 'normal',
  `status` enum('finished','inprocess','none') NOT NULL DEFAULT 'none'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `employee`
--

CREATE TABLE `employee` (
  `id` int(11) UNSIGNED NOT NULL,
  `name` varchar(30) NOT NULL,
  `surname` varchar(30) NOT NULL,
  `position` enum('Manager','Receptionist','Cleaner') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `employee`
--

INSERT INTO `employee` (`id`, `name`, `surname`, `position`) VALUES
(1, 'deflaut', 'daflaut', 'Manager'),

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `guest`
--

CREATE TABLE `guest` (
  `id` int(11) UNSIGNED NOT NULL,
  `name` varchar(30) NOT NULL,
  `surname` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `hibernate_sequence`
--

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `room`
--

CREATE TABLE `room` (
  `id` int(11) UNSIGNED NOT NULL,
  `floor` int(2) NOT NULL,
  `type` int(1) UNSIGNED NOT NULL,
  `price` double(6,2) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `room`
--

INSERT INTO `room` (`id`, `floor`, `type`, `price`) VALUES
(1, 1, 1, 100.00),
(2, 1, 2, 100.00),
(3, 1, 2, 100.00),
(4, 1, 2, 100.00);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `user`
--

CREATE TABLE `user` (
  `employeeId` int(11) UNSIGNED NOT NULL,
  `login` varchar(50) NOT NULL,
  `password` text NOT NULL,
  `email` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `user`
--

INSERT INTO `user` (`employeeId`, `login`, `password`, `email`) VALUES
(1, 'manager', '$argon2id$v=19$m=65,t=1,p=1$lTS/cm/PRxNmDlmlBi2sTA$5xQgTdtmniMWcb0LHSMYpIgMVdplkJ3DG4dOul3Ywrg', 'manager@hotel.pl');

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `booking`
--
ALTER TABLE `booking`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKhmve27220rv1ruel949hg43a6` (`guestId`),
  ADD KEY `FKm78aecdw3kvsi0gfnvghnokqb` (`roomId`);

--
-- Indeksy dla tabeli `cleaning`
--
ALTER TABLE `cleaning`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKscxbemwbmv96a6ia87lbgdsk4` (`employeeId`),
  ADD KEY `FKrg93p5medi8fb1epmedkr3nu2` (`roomId`);

--
-- Indeksy dla tabeli `employee`
--
ALTER TABLE `employee`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `guest`
--
ALTER TABLE `guest`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `room`
--
ALTER TABLE `room`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`login`),
  ADD UNIQUE KEY `login` (`login`),
  ADD KEY `FKe7nyol99xk1owsiasrh9ghxql` (`employeeId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT dla tabeli `booking`
--
ALTER TABLE `booking`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;

--
-- AUTO_INCREMENT dla tabeli `cleaning`
--
ALTER TABLE `cleaning`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;

--
-- AUTO_INCREMENT dla tabeli `employee`
--
ALTER TABLE `employee`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT dla tabeli `guest`
--
ALTER TABLE `guest`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;

--
-- AUTO_INCREMENT dla tabeli `room`
--
ALTER TABLE `room`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `booking`
--
ALTER TABLE `booking`
  ADD CONSTRAINT `FKhmve27220rv1ruel949hg43a6` FOREIGN KEY (`guestId`) REFERENCES `guest` (`id`),
  ADD CONSTRAINT `FKm78aecdw3kvsi0gfnvghnokqb` FOREIGN KEY (`roomId`) REFERENCES `room` (`id`);

--
-- Ograniczenia dla tabeli `cleaning`
--
ALTER TABLE `cleaning`
  ADD CONSTRAINT `FKrg93p5medi8fb1epmedkr3nu2` FOREIGN KEY (`roomId`) REFERENCES `room` (`id`),
  ADD CONSTRAINT `FKscxbemwbmv96a6ia87lbgdsk4` FOREIGN KEY (`employeeId`) REFERENCES `employee` (`id`);

--
-- Ograniczenia dla tabeli `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `FKe7nyol99xk1owsiasrh9ghxql` FOREIGN KEY (`employeeId`) REFERENCES `employee` (`id`);

DELIMITER $$
--
-- Zdarzenia
--
CREATE DEFINER=`root`@`localhost` EVENT `RemoveGuestWhoCheckedOut` ON SCHEDULE EVERY 7 DAY STARTS '2020-11-01 10:09:14' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN 
DELETE `guest`,`booking` FROM `guest`,`booking` WHERE guest.id=booking.guestId AND booking.hasCkeckedOut =1 AND CURRENT_DATE > DATE_ADD(booking.timeOfCheckOut,INTERVAL 7 DAY);

DELETE guest.* FROM guest LEFT JOIN booking ON booking.guestId=guest.id WHERE  booking.id IS null;
END$$

CREATE DEFINER=`root`@`localhost` EVENT `UpdateUrgencyInCleaning` ON SCHEDULE EVERY 1 DAY STARTS '2020-11-02 00:01:00' ON COMPLETION NOT PRESERVE ENABLE DO CALL UpdateUrgencyInCleaning()$$

DELIMITER ;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
