-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : jeu. 06 mai 2021 à 19:43
-- Version du serveur :  10.4.18-MariaDB
-- Version de PHP : 7.3.27

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `packappdb`
--

-- --------------------------------------------------------

--
-- Structure de la table `commandes`
--

CREATE TABLE `commandes` (
  `fullName` varchar(40) NOT NULL,
  `phone` int(11) NOT NULL,
  `email` varchar(50) NOT NULL,
  `address` varchar(100) NOT NULL,
  `gender` varchar(30) NOT NULL,
  `product` varchar(40) NOT NULL,
  `price` double NOT NULL,
  `amount` int(100) NOT NULL,
  `status` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `commandes`
--

INSERT INTO `commandes` (`fullName`, `phone`, `email`, `address`, `gender`, `product`, `price`, `amount`, `status`) VALUES
('Alouce', 7654, 'mst', 'Dhar', 'Femme', '', 0, 0, 'Livrée'),
('alouche', 87, 'mms', 'lot ', 'Homme', 'En cours', 0, 0, 'PC portable'),
('a', 8, 'j', 'ljj', 'Homme', 'PC portable', 13, 4, 'Livrée'),
('YUt', 7676, 'uyt', 'sdf', 'Homme', 'PC Portable', 76, 11, 'Livrée');

-- --------------------------------------------------------

--
-- Structure de la table `products`
--

CREATE TABLE `products` (
  `product` varchar(40) NOT NULL,
  `price` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `products`
--

INSERT INTO `products` (`product`, `price`) VALUES
('PC Portable', 3500),
('PC Breau', 2000),
('Téléphone', 2750),
('TV', 6500),
('Tablette', 3000);

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE `users` (
  `username` varchar(40) NOT NULL,
  `password` varchar(40) NOT NULL,
  `oldPassword` varchar(40) NOT NULL,
  `email` varchar(50) NOT NULL DEFAULT 'Admin_Manager@gmail.com'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `users`
--

INSERT INTO `users` (`username`, `password`, `oldPassword`, `email`) VALUES
('admin', '2000/08/01', '1234', 'Admin_Manager@gmail.com');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
