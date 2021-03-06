
package org.jboss.qa.tool.saatr.repo.config;

import org.bson.types.ObjectId;
import org.jboss.qa.tool.saatr.domain.config.ConfigDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * A repository interface assembling CRUD functionality as well as the API to invoke the
 * methods implemented manually.
 * 
 * @author dsimko@redhat.com
 */
public interface ConfigRepository extends MongoRepository<ConfigDocument, ObjectId>, ConfigRepositoryCustom {

}
