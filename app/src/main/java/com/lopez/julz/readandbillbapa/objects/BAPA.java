package com.lopez.julz.readandbillbapa.objects;

public class BAPA {
    private String OrganizationParentAccount;

    public BAPA() {
    }

    public BAPA(String organizationParentAccount) {
        OrganizationParentAccount = organizationParentAccount;
    }

    public String getOrganizationParentAccount() {
        return OrganizationParentAccount;
    }

    public void setOrganizationParentAccount(String organizationParentAccount) {
        OrganizationParentAccount = organizationParentAccount;
    }
}
