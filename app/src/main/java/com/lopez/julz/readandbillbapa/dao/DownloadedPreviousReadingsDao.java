package com.lopez.julz.readandbillbapa.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DownloadedPreviousReadingsDao {
    @Query("SELECT * FROM DownloadedPreviousReadings")
    List<DownloadedPreviousReadings> getAll();

    @Query("SELECT * FROM DownloadedPreviousReadings WHERE id = :id")
    DownloadedPreviousReadings getOne(String id);

    @Insert
    void insertAll(DownloadedPreviousReadings... downloadedPreviousReadings);

    @Update
    void updateAll(DownloadedPreviousReadings... downloadedPreviousReadings);

    @Query("DELETE FROM DownloadedPreviousReadings WHERE ServicePeriod = :servicePeriod")
    void deleteAllByServicePeriod(String servicePeriod);

    @Query("SELECT * FROM DownloadedPreviousReadings WHERE ServicePeriod = :servicePeriod AND OrganizationParentAccount = :bapaName ORDER BY ServiceAccountName")
    List<DownloadedPreviousReadings> getAllFromSchedule(String servicePeriod, String bapaName);

    @Query("SELECT * FROM DownloadedPreviousReadings WHERE CAST(SequenceCode AS INT) > :sequenceCode AND OrganizationParentAccount = :bapaName ORDER BY SequenceCode LIMIT 1")
    DownloadedPreviousReadings getNext(int sequenceCode, String bapaName);

    @Query("SELECT * FROM DownloadedPreviousReadings WHERE CAST(SequenceCode AS INT) < :sequenceCode AND OrganizationParentAccount = :bapaName ORDER BY SequenceCode DESC LIMIT 1")
    DownloadedPreviousReadings getPrevious(int sequenceCode, String bapaName);

    @Query("SELECT * FROM DownloadedPreviousReadings WHERE OrganizationParentAccount = :bapaName ORDER BY SequenceCode LIMIT 1")
    DownloadedPreviousReadings getFirst(String bapaName);

    @Query("SELECT * FROM DownloadedPreviousReadings WHERE OrganizationParentAccount = :bapaName ORDER BY SequenceCode DESC LIMIT 1")
    DownloadedPreviousReadings getLast(String bapaName);

    @Query("SELECT * FROM DownloadedPreviousReadings WHERE ServicePeriod = :servicePeriod AND OrganizationParentAccount = :bapaName AND Status IS NULL")
    List<DownloadedPreviousReadings> getAllUnread(String servicePeriod, String bapaName);
}
