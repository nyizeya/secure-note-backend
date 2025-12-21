SELECT 'CREATE DATABASE notes'
    WHERE NOT EXISTS (
    SELECT FROM pg_database WHERE datname = 'notes'
)\gexec
