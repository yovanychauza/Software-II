/* Creamos algunos usuarios con sus roles */
INSERT INTO `usuarios` (username, password, enabled, nombre, apellido, email) VALUES ('danielp','$2a$10$1pGujr84her6y/1L4btXmOrbqUlQ7IM5nZDHbbaYasPD9Uq7gHv0e',1, 'Daniel', 'Paz','danielp@unicauca.edu.co');
INSERT INTO `usuarios` (username, password, enabled, nombre, apellido, email) VALUES ('pmage','$2a$10$g/sntmRxc9hr45nyAQtA1eW4.HZetTmjuwb7nZ0jLH.6PyZt1KyeC',1, 'Pablo', 'Mage','pmage@unicauca.edu.co');

INSERT INTO `roles` (nombre) VALUES ('ROLE_USER');
INSERT INTO `roles` (nombre) VALUES ('ROLE_ADMIN');

/*Daniel rol de usuario, mage rol de usuario y administrador*/
INSERT INTO `usuarios_roles` (usuario_id, role_id) VALUES (1, 1);
INSERT INTO `usuarios_roles` (usuario_id, role_id) VALUES (2, 2);
INSERT INTO `usuarios_roles` (usuario_id, role_id) VALUES (2, 1);
