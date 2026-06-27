USE lab12;
GO

IF OBJECT_ID('dbo.Videojuego', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Videojuego (
        idVideojuego INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        Consola NVARCHAR(45) NOT NULL,
        Nombre NVARCHAR(45) NOT NULL,
        Genero NVARCHAR(45) NULL,
        Clasificacion NVARCHAR(45) NULL,
        Descripcion NVARCHAR(45) NULL,
        IDdesarrollador INT NOT NULL,
        IDdistribuidor INT NOT NULL,
        CONSTRAINT FK_Videojuego_Distribuidor
            FOREIGN KEY (IDdistribuidor)
            REFERENCES dbo.Distribuidor(IdDistribuidor)
    );
END;
GO

