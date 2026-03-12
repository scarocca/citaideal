-- INSERTAR PLANES DE CITAS (Servicios de CitaIdeal.cl)
INSERT INTO servicios (nombre, descripcion, duracion_minutos, precio, activo) 
VALUES (
    'Plan Atardecer de Ensueño', 
    'Cena privada a la orilla del mar al atardecer. Incluye: Decoración boho-chic, tabla de quesos premium, botella de espumante y música ambiental.', 
    120, 85000.00, true
);

INSERT INTO servicios (nombre, descripcion, duracion_minutos, precio, activo) 
VALUES (
    'Plan Velada Estelar', 
    'Experiencia nocturna bajo las estrellas. Incluye: Cena de 3 tiempos, fogata privada, telescopio para observación y fotografía profesional de la pareja.', 
    180, 150000.00, true
);

INSERT INTO servicios (nombre, descripcion, duracion_minutos, precio, activo) 
VALUES (
    'Plan Propuesta Real', 
    'Diseñado para peticiones de mano. Incluye: Montaje de lujo con flores naturales, violinista en vivo, cena gourmet y letras gigantes con luces (Marry Me).', 
    240, 350000.00, true
);

-- INSERTAR DISPONIBILIDAD PARA EL AGENTE IA
-- Bloque 1: Disponible para Plan Atardecer mañana
INSERT INTO disponibilidad (fecha, hora_inicio, hora_fin, estado, servicio_id) 
VALUES ('2026-03-13', '19:00:00', '21:00:00', 'DISPONIBLE', 1);

-- Bloque 2: Disponible para Velada Estelar el sábado
INSERT INTO disponibilidad (fecha, hora_inicio, hora_fin, estado, servicio_id) 
VALUES ('2026-03-14', '21:00:00', '00:00:00', 'DISPONIBLE', 2);

-- Bloque 3: Reservado (Ejemplo de horario ocupado)
INSERT INTO disponibilidad (fecha, hora_inicio, hora_fin, estado, servicio_id) 
VALUES ('2026-03-13', '20:00:00', '23:00:00', 'RESERVADO', 3);