package models.internal;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Base class for this application model objects. Automatically provides a @Id Long id field
 *
 * @since 1.0
 */
@SuppressWarnings("WeakerAccess") //public by design
@MappedSuperclass
public class HzModel extends HzGenericModel {

    @Id
    @GeneratedValue
    public Long id;

    public Long getId() {
        return id;
    }

    @Override
    public Object _key() {
        return getId();
    }
}
