package com.project.food.db;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import com.project.food.db.entities.CategoryEntity;
import com.project.food.db.entities.OfferEntity;
import com.project.food.db.entities.ParamEntity;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by kostya on 08.12.2016.
 */

public class OrmliteDBConfigUtils extends OrmLiteConfigUtil {
    /**
     * classes represents the models to use for generating the ormlite_config.txt file
     */
    private static final Class<?>[] classes = new Class[] {CategoryEntity.class,
            OfferEntity.class, ParamEntity.class};

    /**
     * Given that this is a separate program from the android app, we have to use
     * a static main java method to create the configuration file.
     * @param args
     * @throws IOException
     * @throws SQLException
     */
    public static void main(String[] args) throws IOException, SQLException {

        String currDirectory = "user.dir";

        String configPath = "/app/src/main/res/raw/ormlite_config.txt";

        /**
         * Gets the project root directory
         */
        String projectRoot = System.getProperty(currDirectory);

        /**
         * Full configuration path includes the project root path, and the location
         * of the ormlite_config.txt file appended to it
         */
        String fullConfigPath = projectRoot + configPath;

        File configFile = new File(fullConfigPath);

        /**
         * In the a scenario where we run this program serveral times, it will recreate the
         * configuration file each time with the updated configurations.
         */
        if(configFile.exists()) {
            configFile.delete();
            configFile = new File(fullConfigPath);
        }

        /**
         * writeConfigFile is a util method used to write the necessary configurations
         * to the ormlite_config.txt file.
         */
        writeConfigFile(configFile, classes);
    }
}
