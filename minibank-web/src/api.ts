export type Account = {
  id: number,
  ownerId: number,
  iban: string, 
  createdAt: string
}

export type VisibleAccountDetails = {
  iban: string,
  balance: number,
  createdAt: string
}

export interface Owner {
  ownerId: number,
  firstname: string,
  surname: string,
  email: string,
  phonenumber: string,
  createdAt: string
}

export interface OwnerInput {
  firstname: string,
  surname: string,
  email: string,
  phonenumber: string
}

export async function getHealth(): Promise<string> {
  const r = await fetch('/api/health');
  return r.text();
}


export async function loginOwnerWithEmail(email: string): Promise<number> {
  const r = await fetch('/api/login/email', {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ email }),
  });
  const data = await r.json().catch(() => ({}));
  if (!r.ok) {
    throw new Error("\nError type: " + data.type + "\nDetails: " + data.details);
  }
  return data.data.ownerId;
}

export async function getOwnerFromOwnerId(ownerId: number): Promise<Owner> {
  const r = await fetch('/api/owner/data', {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ "ownerId": ownerId })
  });
  const data = await r.json().catch(() => ({}));
  if (!r.ok) {
    throw new Error("\nError type: " + data.type + "\nDetails: " + data.details);
  }
  return {
    ownerId: data.data.ownerId,
    firstname: data.data.firstname,
    surname: data.data.surname,
    email: data.data.email,
    phonenumber: data.data.phonenumber,
    createdAt: data.data.createdAt
  };
}


export async function getBalance(accountId: number): Promise<number> {
  const r = await fetch(`/api/accounts/${accountId}/balance`);
  return r.json();
}

export async function getAccounts(ownerId: number): Promise<Account[]> {
  const r = await fetch(`/api/owners/${ownerId}/accounts`);
  return r.json();
}

export async function createOwner(input: OwnerInput): Promise<any> {
  const r = await fetch("/api/owners", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(input),
  });
  const data = await r.json().catch(() => ({}));
  if (!r.ok) {
    throw new Error("\nError type: " + data.error + "\nDetails: " + data.details);
  }
  
  return data;
} 

