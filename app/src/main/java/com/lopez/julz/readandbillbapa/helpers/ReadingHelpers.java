package com.lopez.julz.readandbillbapa.helpers;

import android.util.Log;

import com.lopez.julz.readandbillbapa.dao.Bills;
import com.lopez.julz.readandbillbapa.dao.DownloadedPreviousReadings;
import com.lopez.julz.readandbillbapa.dao.Rates;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ReadingHelpers {
    public static String getKwhUsed(DownloadedPreviousReadings dprPrev, Double current) {
        try {
            String kwUsed = dprPrev.getKwhUsed() != null ? dprPrev.getKwhUsed() : "0";
            Double prev = Double.valueOf(kwUsed);
            return (current - prev) + "";
        } catch (Exception e) {
            Log.e("ERR_GET_KWH", e.getMessage());
            return "";
        }
    }

    public static String generateBillNumber(String areaCode) {
        try {
            String time = new Date().getTime() + "";
            return areaCode + "" + time.substring(6, time.length()-1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getServiceFromFromServicePeriod(String servicePeriod) {
        try {
            servicePeriod = servicePeriod.substring(0, 6) + "-24";
            Calendar c = new GregorianCalendar();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            c.setTime(sdf.parse(servicePeriod));
            c.add(Calendar.MONTH, -1);
            return sdf.format(c.getTime());
        } catch (Exception e) {
            Log.e("ERR_GEN_SVC_FROM", e.getMessage());
            return "";
        }
    }

    public static String getServiceFromToday() {
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            c.setTime(new Date());
            c.add(Calendar.MONTH, -1);
            return sdf.format(c.getTime());
        } catch (Exception e) {
            Log.e("ERR_GEN_SVC_FROM", e.getMessage());
            return "";
        }
    }

    public static String getServiceTo() {
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            c.setTime(new Date());
            c.add(Calendar.DATE, -1);
            return sdf.format(c.getTime());
        } catch (Exception e) {
            Log.e("ERR_GEN_SVC_FROM", e.getMessage());
            return "";
        }
    }

    public static String getDueDate(String readDate) {
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            c.setTime(new Date());
            c.add(Calendar.DATE, +9);
            return sdf.format(c.getTime());
        } catch (Exception e) {
            Log.e("ERR_GEN_SVC_FROM", e.getMessage());
            return "";
        }
    }

    public static String getLifelineRate(DownloadedPreviousReadings dpr, Bills bill, Rates rate) {
        try {
            double kwhUsed = Double.valueOf(bill.getKwhUsed()) * Double.valueOf(bill.getMultiplier());

            double deductibles = Double.valueOf(bill.getGenerationSystemCharge()) +
                    Double.valueOf(bill.getTransmissionDeliveryChargeKWH()) +
                    Double.valueOf(bill.getTransmissionDeliveryChargeKW()) +
                    Double.valueOf(bill.getSystemLossCharge()) +
                    Double.valueOf(bill.getOtherGenerationRateAdjustment()) +
                    Double.valueOf(bill.getOtherTransmissionCostAdjustmentKW()) +
                    Double.valueOf(bill.getOtherTransmissionCostAdjustmentKWH()) +
                    Double.valueOf(bill.getOtherSystemLossCostAdjustment()) +
                    Double.valueOf(bill.getDistributionDemandCharge()) +
                    Double.valueOf(bill.getDistributionSystemCharge()) +
                    Double.valueOf(bill.getSupplyRetailCustomerCharge()) +
                    Double.valueOf(bill.getSupplySystemCharge()) +
                    Double.valueOf(bill.getMeteringSystemCharge());

            if (dpr.getAccountType() == "RESIDENTIAL") {
                return ObjectHelpers.roundFourNoComma(kwhUsed * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getLifelineRate())));
            } else {
                if (kwhUsed < 15) {
                    return "-" + ObjectHelpers.roundFour(deductibles * .5);
                } else if (kwhUsed >= 16 && kwhUsed < 17) {
                    return "-" + ObjectHelpers.roundTwo(deductibles * .4);
                } else if (kwhUsed >= 17 && kwhUsed < 18) {
                    return "-" + ObjectHelpers.roundTwo(deductibles * .3);
                } else if (kwhUsed >= 18 && kwhUsed < 19) {
                    return "-" + ObjectHelpers.roundTwo(deductibles * .2);
                } else if (kwhUsed >= 19 && kwhUsed < 20) {
                    return "-" + ObjectHelpers.roundTwo(deductibles * .15);
                } else if (kwhUsed >= 20 && kwhUsed < 21) {
                    return "-" + ObjectHelpers.roundTwo(deductibles * .1);
                } else if (kwhUsed >= 21 && kwhUsed < 25) {
                    return "-" + ObjectHelpers.roundTwo(deductibles * .05);
                } else {
                    return ObjectHelpers.roundFourNoComma(kwhUsed * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getLifelineRate())));
                }
            }

        } catch (Exception e) {
            Log.e("ERR_GET_LFLNE_RTE", e.getMessage());
            return "0";
        }
    }

    public static String getSeniorCitizenDiscount(DownloadedPreviousReadings dpr, Bills bill, Rates rate) {
        try {
            double kwhUsed = Double.valueOf(bill.getKwhUsed()) * Double.valueOf(bill.getMultiplier());

            double deductibles = Double.valueOf(bill.getGenerationSystemCharge()) +
                    Double.valueOf(bill.getTransmissionDeliveryChargeKWH()) +
                    Double.valueOf(bill.getTransmissionDeliveryChargeKW()) +
                    Double.valueOf(bill.getSystemLossCharge()) +
                    Double.valueOf(bill.getOtherGenerationRateAdjustment()) +
                    Double.valueOf(bill.getOtherTransmissionCostAdjustmentKW()) +
                    Double.valueOf(bill.getOtherTransmissionCostAdjustmentKWH()) +
                    Double.valueOf(bill.getOtherSystemLossCostAdjustment()) +
                    Double.valueOf(bill.getDistributionDemandCharge()) +
                    Double.valueOf(bill.getDistributionSystemCharge()) +
                    Double.valueOf(bill.getSupplyRetailCustomerCharge()) +
                    Double.valueOf(bill.getSupplySystemCharge()) +
                    Double.valueOf(bill.getMeteringSystemCharge()) +
                    Double.valueOf(bill.getMeteringRetailCustomerCharge());

            if (dpr.getSeniorCitizen() != null && dpr.getSeniorCitizen().equals("Yes") && kwhUsed <= 100) {
                return "-" + (deductibles * .05);
            } else {
                return ObjectHelpers.roundFourNoComma(kwhUsed * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getSeniorCitizenSubsidy())));
            }
        } catch (Exception e) {
            Log.e("ERR_GET_SC_DSCNT", e.getMessage());
            return "0";
        }
    }

    public static String getNetAmount(DownloadedPreviousReadings dpr, Bills bill) {
        try {
            double amount = 0.0;

            String additionalCharges = bill.getAdditionalCharges() != null ? bill.getAdditionalCharges() : "0";

            amount = Double.valueOf(bill.getGenerationSystemCharge()) +
                Double.valueOf(bill.getTransmissionDeliveryChargeKW()) +
                Double.valueOf(bill.getTransmissionDeliveryChargeKWH()) +
                Double.valueOf(bill.getSystemLossCharge()) +
                Double.valueOf(bill.getDistributionDemandCharge()) +
                Double.valueOf(bill.getDistributionSystemCharge()) +
                Double.valueOf(bill.getSupplyRetailCustomerCharge()) +
                Double.valueOf(bill.getSupplySystemCharge()) +
                Double.valueOf(bill.getMeteringRetailCustomerCharge()) +
                Double.valueOf(bill.getMeteringSystemCharge()) +
                Double.valueOf(bill.getRFSC()) +
                Double.valueOf(bill.getInterClassCrossSubsidyCharge()) +
                Double.valueOf(bill.getPPARefund()) +
                Double.valueOf(bill.getMissionaryElectrificationCharge()) +
                Double.valueOf(bill.getEnvironmentalCharge()) +
                Double.valueOf(bill.getStrandedContractCosts()) +
                Double.valueOf(bill.getNPCStrandedDebt()) +
                Double.valueOf(bill.getFeedInTariffAllowance()) +
                Double.valueOf(bill.getMissionaryElectrificationREDCI()) +
                Double.valueOf(bill.getGenerationVAT()) +
                Double.valueOf(bill.getTransmissionVAT()) +
                Double.valueOf(bill.getSystemLossVAT()) +
                Double.valueOf(bill.getDistributionVAT()) +
                Double.valueOf(bill.getRealPropertyTax()) +
                Double.valueOf(bill.getOtherGenerationRateAdjustment()) +
                Double.valueOf(bill.getOtherTransmissionCostAdjustmentKW()) +
                Double.valueOf(bill.getOtherTransmissionCostAdjustmentKWH()) +
                Double.valueOf(bill.getOtherSystemLossCostAdjustment()) +
                Double.valueOf(bill.getOtherLifelineRateCostAdjustment()) +
                Double.valueOf(bill.getSeniorCitizenDiscountAndSubsidyAdjustment()) +
                Double.valueOf(bill.getFranchiseTax()) +
                Double.valueOf(bill.getBusinessTax()) +
                Double.valueOf(bill.getSeniorCitizenSubsidy()) +
                Double.valueOf(bill.getLifelineRate()) +
                Double.valueOf(additionalCharges);

            return ObjectHelpers.roundFourNoComma(amount);
        } catch (Exception e) {
            return "0";
        }
    }

    public static String getDistributionVat(Bills bills) {
        try {
            double vatables = Double.valueOf(bills.getDistributionSystemCharge()) +
                    Double.valueOf(bills.getSupplySystemCharge()) +
                    Double.valueOf(bills.getMeteringRetailCustomerCharge()) +
                    Double.valueOf(bills.getMeteringSystemCharge()) +
                    Double.valueOf(bills.getLifelineRate()) +
                    Double.valueOf(bills.getOtherLifelineRateCostAdjustment());

            return ObjectHelpers.roundFourNoComma(vatables * .12);
        } catch (Exception e) {
            Log.e("ERR_GET_DST_VAT", e.getMessage());
            return "0";
        }
    }

    public static Bills generateRegularBill(Bills bill, DownloadedPreviousReadings dpr, Rates rate, Double kwhUsed, Double presReading, String userId) {
        try {
            if (rate != null) {

                double effectiveRate = Double.valueOf(rate.getTotalRateVATIncluded());
                double multiplier = Double.valueOf(dpr.getMultiplier());
                String coreLossRaw = dpr.getCoreloss() != null ? dpr.getCoreloss() : "0";
                double coreloss = Double.valueOf(coreLossRaw);
                double kwhUsedFinal = multiplier * kwhUsed;

                String arrearsLedger = dpr.getArrearsLedger() != null ? dpr.getArrearsLedger() : "0";
                double additionalCharges = Double.valueOf(arrearsLedger);

                if (bill != null) {
                    bill.setKwhUsed(kwhUsed + "");
                    bill.setPreviousKwh(dpr.getKwhUsed());
                    bill.setPresentKwh(presReading + "");
                    bill.setKwhAmount(ObjectHelpers.roundFourNoComma(effectiveRate * kwhUsedFinal));
                    bill.setEffectiveRate(ObjectHelpers.roundFourNoComma(effectiveRate));
                    bill.setAdditionalCharges(ObjectHelpers.roundFourNoComma(additionalCharges));
                    bill.setDeductions("");

                    bill.setBillingDate(ObjectHelpers.getCurrentDate());
                    bill.setServiceDateFrom(dpr.getReadingTimestamp() != null ? dpr.getReadingTimestamp() : getServiceFromToday());
                    bill.setServiceDateTo(ObjectHelpers.getCurrentDate());
                    bill.setDueDate(getDueDate(ObjectHelpers.getCurrentDate()));
                    bill.setMeterNumber(dpr.getMeterSerial());
                    bill.setConsumerType(dpr.getAccountType());
                    bill.setBillType(dpr.getAccountType());

                    // COMPUTE RATES
                    bill.setGenerationSystemCharge(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getGenerationSystemCharge()))));
                    bill.setTransmissionDeliveryChargeKW(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getTransmissionDeliveryChargeKW()))));
                    bill.setTransmissionDeliveryChargeKWH(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getTransmissionDeliveryChargeKWH()))));
                    bill.setSystemLossCharge(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getSystemLossCharge()))));
                    bill.setDistributionDemandCharge(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getDistributionDemandCharge()))));
                    bill.setDistributionSystemCharge(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getDistributionSystemCharge()))));
                    bill.setSupplyRetailCustomerCharge(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getSupplyRetailCustomerCharge()))));
                    bill.setSupplySystemCharge(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getSupplySystemCharge()))));
                    bill.setMeteringRetailCustomerCharge(ObjectHelpers.roundFourNoComma(Double.valueOf(ObjectHelpers.doubleStringNull(rate.getMeteringRetailCustomerCharge()))));
                    bill.setMeteringSystemCharge(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getMeteringSystemCharge()))));
                    bill.setRFSC(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getRFSC()))));
                    bill.setInterClassCrossSubsidyCharge(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getInterClassCrossSubsidyCharge()))));
                    bill.setPPARefund(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getPPARefund()))));
                    bill.setMissionaryElectrificationCharge(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getMissionaryElectrificationCharge()))));
                    bill.setEnvironmentalCharge(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getEnvironmentalCharge()))));
                    bill.setStrandedContractCosts(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getStrandedContractCosts()))));
                    bill.setNPCStrandedDebt(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getNPCStrandedDebt()))));
                    bill.setFeedInTariffAllowance(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getFeedInTariffAllowance()))));
                    bill.setMissionaryElectrificationREDCI(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getMissionaryElectrificationREDCI()))));
                    bill.setGenerationVAT(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getGenerationVAT()))));
                    bill.setTransmissionVAT(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getTransmissionVAT()))));
                    bill.setSystemLossVAT(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getSystemLossVAT()))));
                    bill.setRealPropertyTax(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getRealPropertyTax()))));

                    bill.setOtherGenerationRateAdjustment(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getOtherGenerationRateAdjustment()))));
                    bill.setOtherTransmissionCostAdjustmentKW(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getOtherTransmissionCostAdjustmentKW()))));
                    bill.setOtherTransmissionCostAdjustmentKWH(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getOtherTransmissionCostAdjustmentKWH()))));
                    bill.setOtherSystemLossCostAdjustment(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getOtherSystemLossCostAdjustment()))));

                    if (dpr.getSeniorCitizen()  != null && dpr.getSeniorCitizen().equals("Yes") && kwhUsedFinal <= 100) {
                        bill.setOtherLifelineRateCostAdjustment("0");
                        bill.setSeniorCitizenDiscountAndSubsidyAdjustment("0");
                    } else {
                        bill.setOtherLifelineRateCostAdjustment("0");
                        bill.setSeniorCitizenDiscountAndSubsidyAdjustment(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getSeniorCitizenDiscountAndSubsidyAdjustment()))));
                    }

                    bill.setFranchiseTax(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getFranchiseTax()))));
                    bill.setBusinessTax(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getBusinessTax()))));

                    bill.setSeniorCitizenSubsidy(getSeniorCitizenDiscount(dpr, bill, rate));
                    bill.setLifelineRate(getLifelineRate(dpr, bill, rate));

                    bill.setDistributionVAT(getDistributionVat(bill));

                    bill.setNetAmount(getNetAmount(dpr, bill));

                    /**
                     * FOR DEPOSITS/PREPAYMENTS
                     */
                    if (dpr.getDeposit() != null) {
                        double depositAmount = Double.valueOf(dpr.getDeposit());
                        double netAmnt = Double.valueOf(bill.getNetAmount());
                        double difOfNetAmount = netAmnt - depositAmount;

                        if (difOfNetAmount > 0) {
                            bill.setNetAmount(ObjectHelpers.roundFourNoComma(difOfNetAmount));
                            bill.setDeductedDeposit(ObjectHelpers.roundFourNoComma(depositAmount));
                            bill.setExcessDeposit("0");
                        } else {
                            double depositVal = depositAmount - netAmnt;
                            bill.setNetAmount("0.0");
                            bill.setDeductedDeposit(ObjectHelpers.roundFourNoComma(netAmnt));
                            bill.setExcessDeposit(ObjectHelpers.roundFourNoComma(depositVal));
                        }
                    }

                    bill.setUserId(userId);
                    bill.setBilledFrom("APP");
                    bill.setUploadStatus("UPLOADABLE");
                } else {
                    bill  = new Bills();

                    bill.setId(ObjectHelpers.generateIDandRandString());
                    bill.setBillNumber(generateBillNumber(dpr.getAreaCode()));
                    bill.setAccountNumber(dpr.getId());
                    bill.setServicePeriod(dpr.getServicePeriod());
                    bill.setMultiplier(dpr.getMultiplier());
                    bill.setCoreloss(coreLossRaw);
                    bill.setKwhUsed(kwhUsed + "");
                    bill.setPreviousKwh(dpr.getKwhUsed());
                    bill.setPresentKwh(presReading + "");
                    bill.setKwhAmount(ObjectHelpers.roundFourNoComma(effectiveRate * kwhUsedFinal));
                    bill.setEffectiveRate(ObjectHelpers.roundFourNoComma(effectiveRate));
                    bill.setAdditionalCharges(ObjectHelpers.roundFourNoComma(additionalCharges));
                    bill.setDeductions("");

                    bill.setBillingDate(ObjectHelpers.getCurrentDate());
                    bill.setServiceDateFrom(dpr.getReadingTimestamp() != null ? dpr.getReadingTimestamp() : getServiceFromToday());
                    bill.setServiceDateTo(ObjectHelpers.getCurrentDate());
                    bill.setDueDate(getDueDate(ObjectHelpers.getCurrentDate()));
                    bill.setMeterNumber(dpr.getMeterSerial());
                    bill.setConsumerType(dpr.getAccountType());
                    bill.setBillType(dpr.getAccountType());

                    // COMPUTE RATES
                    bill.setGenerationSystemCharge(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getGenerationSystemCharge()))));
                    bill.setTransmissionDeliveryChargeKW(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getTransmissionDeliveryChargeKW()))));
                    bill.setTransmissionDeliveryChargeKWH(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getTransmissionDeliveryChargeKWH()))));
                    bill.setSystemLossCharge(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getSystemLossCharge()))));
                    bill.setDistributionDemandCharge(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getDistributionDemandCharge()))));
                    bill.setDistributionSystemCharge(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getDistributionSystemCharge()))));
                    bill.setSupplyRetailCustomerCharge(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getSupplyRetailCustomerCharge()))));
                    bill.setSupplySystemCharge(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getSupplySystemCharge()))));
                    bill.setMeteringRetailCustomerCharge(ObjectHelpers.roundFourNoComma(Double.valueOf(ObjectHelpers.doubleStringNull(rate.getMeteringRetailCustomerCharge()))));
                    bill.setMeteringSystemCharge(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getMeteringSystemCharge()))));
                    bill.setRFSC(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getRFSC()))));
                    bill.setInterClassCrossSubsidyCharge(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getInterClassCrossSubsidyCharge()))));
                    bill.setPPARefund(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getPPARefund()))));
                    bill.setMissionaryElectrificationCharge(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getMissionaryElectrificationCharge()))));
                    bill.setEnvironmentalCharge(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getEnvironmentalCharge()))));
                    bill.setStrandedContractCosts(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getStrandedContractCosts()))));
                    bill.setNPCStrandedDebt(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getNPCStrandedDebt()))));
                    bill.setFeedInTariffAllowance(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getFeedInTariffAllowance()))));
                    bill.setMissionaryElectrificationREDCI(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getMissionaryElectrificationREDCI()))));
                    bill.setGenerationVAT(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getGenerationVAT()))));
                    bill.setTransmissionVAT(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getTransmissionVAT()))));
                    bill.setSystemLossVAT(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getSystemLossVAT()))));
                    bill.setRealPropertyTax(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getRealPropertyTax()))));

                    bill.setOtherGenerationRateAdjustment(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getOtherGenerationRateAdjustment()))));
                    bill.setOtherTransmissionCostAdjustmentKW(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getOtherTransmissionCostAdjustmentKW()))));
                    bill.setOtherTransmissionCostAdjustmentKWH(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getOtherTransmissionCostAdjustmentKWH()))));
                    bill.setOtherSystemLossCostAdjustment(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getOtherSystemLossCostAdjustment()))));

                    if (dpr.getSeniorCitizen()  != null && dpr.getSeniorCitizen().equals("Yes") && kwhUsedFinal <= 100) {
                        bill.setOtherLifelineRateCostAdjustment("0");
                        bill.setSeniorCitizenDiscountAndSubsidyAdjustment("0");
                    } else {
                        bill.setOtherLifelineRateCostAdjustment("0");
                        bill.setSeniorCitizenDiscountAndSubsidyAdjustment(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getSeniorCitizenDiscountAndSubsidyAdjustment()))));
                    }

                    bill.setFranchiseTax(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getFranchiseTax()))));
                    bill.setBusinessTax(ObjectHelpers.roundFourNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getBusinessTax()))));

                    bill.setSeniorCitizenSubsidy(getSeniorCitizenDiscount(dpr, bill, rate));
                    bill.setLifelineRate(getLifelineRate(dpr, bill, rate));

                    bill.setDistributionVAT(getDistributionVat(bill));

                    bill.setNetAmount(getNetAmount(dpr, bill));

                    /**
                     * FOR DEPOSITS/PREPAYMENTS
                     */
                    if (dpr.getDeposit() != null) {
                        double depositAmount = Double.valueOf(dpr.getDeposit());
                        double netAmnt = Double.valueOf(bill.getNetAmount());
                        double difOfNetAmount = netAmnt - depositAmount;

                        if (difOfNetAmount > 0) {
                            bill.setNetAmount(ObjectHelpers.roundFourNoComma(difOfNetAmount));
                            bill.setDeductedDeposit(ObjectHelpers.roundFourNoComma(depositAmount));
                            bill.setExcessDeposit("0");
                        } else {
                            double depositVal = depositAmount - netAmnt;
                            bill.setNetAmount("0.0");
                            bill.setDeductedDeposit(ObjectHelpers.roundFourNoComma(netAmnt));
                            bill.setExcessDeposit(ObjectHelpers.roundFourNoComma(depositVal));
                        }
                    }

                    bill.setUserId(userId);
                    bill.setBilledFrom("APP");
                    bill.setUploadStatus("UPLOADABLE");
                }

                return bill;
            } else {

                return null;
            }
        } catch (Exception e) {
            Log.e("ERR_GENRTE_BILL", e.getMessage());
            return null;
        }
    }

    public static String getAccountType(DownloadedPreviousReadings dpr) {
        if (dpr.getAccountType() != null) {
            if (dpr.getAccountType().equals("RURAL RESIDENTIAL") | dpr.getAccountType().equals("NONE")) {
                return "RESIDENTIAL";
            } else {
                return dpr.getAccountType();
            }
        } else {
            return "RESIDENTIAL";
        }
    }
}
