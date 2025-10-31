export type Account = {
  id: number,
  ownerId: number,
  iban: string, 
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

export async function getBalance(accountId: number): Promise<number> {
  const r = await fetch(`/api/accounts/${accountId}/balance`);
  return r.json();
}

export async function getAccounts(ownerId: number): Promise<Account[]> {
  const r = await fetch(`/api/owners/${ownerId}/accounts`);
  return r.json();
}

export async function createOwner(input: OwnerInput): Promise<number> {
  const r = await fetch("/api/owners", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(input),
  });
  if (!r.ok) {
    const err = await r.json().catch(() => ({}));
    throw new Error(err.error || `HTTP ${r.status}`);
  }
  
  const id = await r.json();
  return r.json();
} 

