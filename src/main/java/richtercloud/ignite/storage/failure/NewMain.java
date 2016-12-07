/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package richtercloud.ignite.storage.failure;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author richter
 */
public class NewMain {
    private final static CacheManager MANAGER =
                 Caching.getCachingProvider().getCacheManager();
    private final static Cache<Long, Map<Integer, BufferedImage>> CACHE;
    private final static Cache<Long, Map<Integer, ImageIcon>> CACHE_SERIALIZABLE;
    static {
        MutableConfiguration<Long, Map<Integer, BufferedImage>> config = new MutableConfiguration<>();
        MutableConfiguration<Long, Map<Integer, ImageIcon>> javaFXConfig = new MutableConfiguration<>();
        for(MutableConfiguration config0 : new MutableConfiguration[] {config, javaFXConfig}) {
            config0.setStoreByValue(false)
                    .setStatisticsEnabled(true)
                    .setExpiryPolicyFactory(FactoryBuilder.factoryOf(
                            new AccessedExpiryPolicy(new Duration(TimeUnit.HOURS, 1))));
        }
        CACHE = MANAGER.createCache("cache",
                config);
        CACHE_SERIALIZABLE = MANAGER.createCache("cache-serializable",
                javaFXConfig);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        testCache();
        testCache();
        testCacheSerializable();
        testCacheSerializable();
    }

    private static void testCache() throws IOException {
        Long id = 0L;
        Map<Integer, BufferedImage> cacheEntry = CACHE.get(id);
        if(cacheEntry == null) {
            System.out.println("Cache entry not found");
            cacheEntry = new HashMap<>();
            CACHE.put(id, cacheEntry);
        }else {
            System.out.println("Cache entry found");
        }
        int imageId = 0;
        BufferedImage image = cacheEntry.get(imageId);
        if(image == null) {
            System.out.println("Image not found");
            image = ImageIO.read(NewMain.class.getResource("/File_CC-BY-SA_3_icon_88x31.png"));
            cacheEntry.put(imageId, image);
        }else {
            System.out.println("Image found");
        }
    }

    private static void testCacheSerializable() throws IOException {
        Long id = 0L;
        Map<Integer, ImageIcon> cacheEntry = CACHE_SERIALIZABLE.get(id);
        if(cacheEntry == null) {
            System.out.println("Cache entry not found");
            cacheEntry = new HashMap<>();
            CACHE_SERIALIZABLE.put(id, cacheEntry);
        }else {
            System.out.println("Cache entry found");
        }
        int imageId = 0;
        ImageIcon image = cacheEntry.get(imageId);
        if(image == null) {
            System.out.println("Image not found");
            BufferedImage bufferedImage = ImageIO.read(NewMain.class.getResource("/File_CC-BY-SA_3_icon_88x31.png"));
            image = new ImageIcon(bufferedImage);
            cacheEntry.put(imageId, image);
        }else {
            System.out.println("Image found");
        }
    }
}
