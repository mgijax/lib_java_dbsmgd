package org.jax.mgi.shr.datafactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import org.jax.mgi.shr.log.Logger;
import org.jax.mgi.shr.dto.DTO;
import org.jax.mgi.shr.timing.TimeStamper;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.DBException;



/** The Factory class contains a standard constructor and some utility
*   methods for use in other Factories within the WI.
* @is an abstract factory class providing utility methods to other factories.
* @has a database manager, logger, timer, configuration and name.
* @does nothing.  Is abstract.
*    The following methods must be implemented in any child classes:
*    <OL>
*    <LI> getKey() -- retrieve the key from the cgi params.
*    <LI> getKeyForID() -- find the object key for a given MGI ID
*    <LI> getFullInfo() -- get all available information for a record type
*    <LI> getBasicInfo() -- get a minimal set of available information for
*                           record type
*    </OL>
*/
public abstract class Factory {

    protected SQLDataManager sqlDM;
    protected Logger logger;
    protected DataFactoryCfg config;
    protected TimeStamper timer;
    protected String printName;


    /** constructor; instantiates and initializes a new Factory.
    * @param config provides parameters needed to configure this Factory.
    *        Reqirements will vary depending upon child class.
    * @param sqlDM provides access to a database
    * @param logger provides logging capability
    * @param printName the pretty print name of this factory type used in
    *        logging/timestamping
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    */
    public Factory (DataFactoryCfg config,
                    SQLDataManager sqlDM,
                    Logger logger,
                    String printName) {

        this.config = config;
        this.sqlDM = sqlDM;
        this.logger = logger;
        this.printName = printName;
        this.setTimeStamper(new TimeStamper());

    }

    /** adds the given <tt>TimeStamper</tt> to the Factory and begins
    *    collection of timing data for Factory code
    * @param timer used to collect profiling data for sections of code
    * @return nothing
    * @assumes orintName has been set.
    * @effects nothing
    * @throws nothing
    */
    public void setTimeStamper(TimeStamper timer) {

        this.timer = timer;
        this.timeStamp("TimeStamper added to " + printName);

    }

    /** add the given 'entry' to the TimeStamper in this Factory, if
    *    one was added using setTimeStamper().
    * @param entry the entry to write to the TimeStamper
    * @return nothing
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    */
    protected void timeStamp (String entry) {
        if (this.timer != null) {
            this.timer.record (entry);
        }
    }

    /** combine 's' and 't' into one String, gracefully handling nulls.
    * @param s String to be at the left of the result String
    * @param t String to be at the right of the result String
    * @return String 's' + 't', will be null if both 's' and 't' are null
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    */
    protected static String combine (String s, String t)
    {
    // if 's' is null, then we can just return 't' whether null or not

    if (s == null)
    {
        return t;
    }

    // we know that 's' is not null, so if 't' is null, just return 's'

    else if (t == null)
    {
        return s;
    }

        // otherwise, 's' and 't' are both non-null, so we use both

    return s + t;
    }




}
