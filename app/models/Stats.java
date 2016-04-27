package models;

import com.google.gson.annotations.Since;
import models.internal.HzModel;
import play.data.validation.*;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Stats DB Table
 *
 * @since 1.1
 */
@Entity
@Table(name = "stats")
@Since(1.1)
public class Stats extends HzModel {

    @Unique
    @Required
    @MinSize(value = 5, message = "ident.minsize")
    @MaxSize(value = 255, message = "ident.maxsize")
    public String ident;

    @Required
    @IPv4Address
    public String ip;

    @MinSize(value = 2, message = "country.minsize")
    public String country;
    
    public String browser;

    public String OS;

    @URL
    public String referer;
}
