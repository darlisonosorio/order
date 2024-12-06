DO $$
BEGIN
   IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'order') THEN
      CREATE DATABASE "order";
   END IF;
END $$;

ALTER DATABASE "order" OWNER TO postgres;