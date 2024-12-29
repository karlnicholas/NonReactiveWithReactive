-- V1__Create_Message_Table.sql
CREATE TABLE MESSAGE (
                         id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY, -- Auto-increment primary key
                         message NVARCHAR(MAX) NULL                    -- Message content
);
