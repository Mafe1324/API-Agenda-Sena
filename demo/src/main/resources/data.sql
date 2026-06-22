INSERT INTO ambientes (id, nombre, tipo, capacidad, activo)
SELECT 1001, 'Laboratorio  101', 'LABORATORIO', 30, true
WHERE NOT EXISTS (SELECT 1 FROM ambientes WHERE id = 1001);

INSERT INTO ambientes (id, nombre, tipo, capacidad, activo)
SELECT 1002, 'Sala  201', 'SALA', 25, true
WHERE NOT EXISTS (SELECT 1 FROM ambientes WHERE id = 1002);

INSERT INTO ambientes (id, nombre, tipo, capacidad, activo)
SELECT 1003, 'Auditorio  Principal', 'AUDITORIO', 100, true
WHERE NOT EXISTS (SELECT 1 FROM ambientes WHERE id = 1003);

INSERT INTO ambientes (id, nombre, tipo, capacidad, activo)
SELECT 1004, 'Laboratorio  Inactivo', 'LABORATORIO', 20, false
WHERE NOT EXISTS (SELECT 1 FROM ambientes WHERE id = 1004);

INSERT INTO ambientes (id, nombre, tipo, capacidad, activo)
SELECT 1005, 'Sala  Instructor', 'SALA', 35, true
WHERE NOT EXISTS (SELECT 1 FROM ambientes WHERE id = 1005);

INSERT INTO reservas (id, ambiente_id, nombre_instructor, fecha_inicio, fecha_fin, numero_aprendices, estado)
SELECT 2001, 1001, 'Instructor  Cruce', '2027-07-01 08:00:00', '2027-07-01 10:00:00', 20, 'ACTIVA'
WHERE NOT EXISTS (SELECT 1 FROM reservas WHERE id = 2001);

INSERT INTO reservas (id, ambiente_id, nombre_instructor, fecha_inicio, fecha_fin, numero_aprendices, estado)
SELECT 2002, 1003, 'Instructor  Reporte', '2027-07-02 08:00:00', '2027-07-02 10:00:00', 50, 'ACTIVA'
WHERE NOT EXISTS (SELECT 1 FROM reservas WHERE id = 2002);

INSERT INTO reservas (id, ambiente_id, nombre_instructor, fecha_inicio, fecha_fin, numero_aprendices, estado)
SELECT 2003, 1003, 'Instructor  Reporte', '2027-07-03 08:00:00', '2027-07-03 10:00:00', 50, 'ACTIVA'
WHERE NOT EXISTS (SELECT 1 FROM reservas WHERE id = 2003);

INSERT INTO reservas (id, ambiente_id, nombre_instructor, fecha_inicio, fecha_fin, numero_aprendices, estado)
SELECT 2004, 1005, 'Instructor Limite', '2027-07-04 08:00:00', '2027-07-04 09:00:00', 20, 'ACTIVA'
WHERE NOT EXISTS (SELECT 1 FROM reservas WHERE id = 2004);

INSERT INTO reservas (id, ambiente_id, nombre_instructor, fecha_inicio, fecha_fin, numero_aprendices, estado)
SELECT 2005, 1005, 'Instructor Limite', '2027-07-04 10:00:00', '2027-07-04 11:00:00', 20, 'ACTIVA'
WHERE NOT EXISTS (SELECT 1 FROM reservas WHERE id = 2005);

INSERT INTO reservas (id, ambiente_id, nombre_instructor, fecha_inicio, fecha_fin, numero_aprendices, estado)
SELECT 2006, 1005, 'Instructor Limite', '2027-07-04 12:00:00', '2027-07-04 13:00:00', 20, 'ACTIVA'
WHERE NOT EXISTS (SELECT 1 FROM reservas WHERE id = 2006);

INSERT INTO reservas (id, ambiente_id, nombre_instructor, fecha_inicio, fecha_fin, numero_aprendices, estado)
SELECT 2007, 1002, 'Instructor Cancelacion OK', '2027-07-05 08:00:00', '2027-07-05 10:00:00', 15, 'ACTIVA'
WHERE NOT EXISTS (SELECT 1 FROM reservas WHERE id = 2007);

INSERT INTO reservas (id, ambiente_id, nombre_instructor, fecha_inicio, fecha_fin, numero_aprendices, estado)
SELECT 2008, 1002, 'Instructor Cancelacion Tardia', DATE_ADD(NOW(), INTERVAL 1 HOUR), DATE_ADD(NOW(), INTERVAL 3 HOUR), 15, 'ACTIVA'
WHERE NOT EXISTS (SELECT 1 FROM reservas WHERE id = 2008);
