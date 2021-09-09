export interface User {
  id: string;
  lastName: string;
  firstName: string;
  email: string;
  username: string;
  profileImageUrl: string;
  lastLoginDateDisplay: Date;
  joinDate: Date;
  enabled: boolean;
  nonLocked: boolean;
  role: string;
  authoritites: [];
}

export const EMPTY_USER: User = {
  id: null,
  firstName: '',
  lastName: '',
  username: '',
  email: '',
  role: '',
  profileImageUrl: '',
  enabled: false,
  nonLocked: false,
  authoritites: [],
  lastLoginDateDisplay: null,
  joinDate: null,
};

export interface UserForm {
  lastName: string;
  firstName: string;
  email: string;
  username: string;
  currentUsername?: string;
  password?: string;
  role?: string;
  isEnabled?: boolean;
  isNonLocked?: boolean;
}
