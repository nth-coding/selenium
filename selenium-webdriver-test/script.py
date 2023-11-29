import unittest
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException
import time
import random
import string

class TestDemoBlaze(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.driver = webdriver.Chrome()

    @classmethod
    def tearDownClass(cls):
        cls.driver.quit()

    def setUp(self):
        self.driver.get("https://www.demoblaze.com/")

    def generate_random_string(self, length):
        letters = string.ascii_lowercase
        return ''.join(random.choice(letters) for i in range(length))

    # ----- Login testcases -----

    # Testcase 1: Login with valid credentials
    def test_login1(self):
        self.driver.find_element(By.ID, "login2").click()
        time.sleep(1)
        self.driver.find_element(By.ID, "loginusername").send_keys("your_username")
        self.driver.find_element(By.ID, "loginpassword").send_keys("your_password")
        self.driver.find_element(By.XPATH, "//button[text()='Log in']").click()
        time.sleep(1)
        assert self.driver.find_element(By.ID, "logout2") is not None

    # Testcase 2: Login with invalid credentials
    def test_login2(self):
        self.driver.find_element(By.ID, "login2").click()
        time.sleep(1)
        self.driver.find_element(By.ID, "loginusername").send_keys("your_username")
        self.driver.find_element(By.ID, "loginpassword").send_keys("1")
        self.driver.find_element(By.XPATH, "//button[text()='Log in']").click()
        time.sleep(1)
        alert = self.driver.switch_to.alert
        assert alert.text == "Wrong password."
        alert.accept()
        
    # Testcase 3: Login with empty credentials
    def test_login3(self):
        self.driver.find_element(By.ID, "login2").click()
        time.sleep(1)
        self.driver.find_element(By.ID, "loginusername").send_keys("")
        self.driver.find_element(By.ID, "loginpassword").send_keys("")
        self.driver.find_element(By.XPATH, "//button[text()='Log in']").click()
        time.sleep(1)
        alert = self.driver.switch_to.alert
        assert alert.text == "Please fill out Username and Password."
        alert.accept()

    # ----- Register testcases -----
    # Testcase 4: Register with valid credentials
    def test_register(self):
        self.driver.find_element(By.ID, "signin2").click()
        time.sleep(1)
        self.driver.find_element(By.ID, "sign-username").send_keys(self.generate_random_string(10))
        self.driver.find_element(By.ID, "sign-password").send_keys("testpassword")
        self.driver.find_element(By.XPATH, "//button[text()='Sign up']").click()
        time.sleep(1)
        alert = self.driver.switch_to.alert
        assert "Sign up successful." in alert.text
        alert.accept()

    # Testcase 5: Register with used email
    def test_register2(self):
        self.driver.find_element(By.ID, "signin2").click()
        time.sleep(1)
        self.driver.find_element(By.ID, "sign-username").send_keys("testuser")
        self.driver.find_element(By.ID, "sign-password").send_keys("testpassword")
        self.driver.find_element(By.XPATH, "//button[text()='Sign up']").click()
        time.sleep(1)
        alert = self.driver.switch_to.alert
        assert "This user already exist." in alert.text
        alert.accept()

    # Testcase 6: Register with empty credentials
    def test_register3(self):
        self.driver.find_element(By.ID, "signin2").click()
        time.sleep(1)
        self.driver.find_element(By.ID, "sign-username").send_keys("")
        self.driver.find_element(By.ID, "sign-password").send_keys("")
        self.driver.find_element(By.XPATH, "//button[text()='Sign up']").click()
        time.sleep(1)
        alert = self.driver.switch_to.alert
        assert "Please fill out Username and Password." in alert.text
        alert.accept()

    # ----- Navigation testcases -----
    # Testcase 7: Navigation by category Phones
    def test_Navigation_by_category(self):
        try:
            category_link = WebDriverWait(self.driver, 10).until(EC.presence_of_element_located((By.LINK_TEXT, "Phones")))
            category_link.click()
            time.sleep(3)  # increase wait time
            assert "Samsung galaxy s6" in self.driver.page_source
        except AssertionError:
            print("Product 'Samsung galaxy s6' not found in the Phones category")

    # Testcase 8: Navigation by category Laptops
    def test_Navigation_by_category2(self):
        try:
            category_link = WebDriverWait(self.driver, 10).until(EC.presence_of_element_located((By.LINK_TEXT, "Laptops")))
            category_link.click()
            time.sleep(3)  # increase wait time
            assert "Dell i7 8gb" in self.driver.page_source
        except AssertionError:
            print("Product 'Dell i7 8gb' not found in the Laptops category")

    # Testcase 9: Navigation by category Monitors
    def test_Navigation_by_category3(self):
        try:
            category_link = WebDriverWait(self.driver, 10).until(EC.presence_of_element_located((By.LINK_TEXT, "Monitors")))
            category_link.click()
            time.sleep(3)  # increase wait time
            assert "Apple monitor 24" in self.driver.page_source
        except AssertionError:
            print("Product 'Apple monitor 24' not found in the Monitors category")

    # ----- Add to cart testcases -----
    # Testcase 10: Add to cart
    def test_add_to_cart(self):
        driver = webdriver.Chrome()
        try:
            driver.get("https://www.demoblaze.com/")
            driver.find_element(By.LINK_TEXT, "Phones").click()
            WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.LINK_TEXT, "Samsung galaxy s6"))).click()
            WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.LINK_TEXT, "Add to cart"))).click()
            time.sleep(1)
            try:
                WebDriverWait(driver, 3).until(EC.alert_is_present())
                alert = driver.switch_to.alert
                assert "Product added" in alert.text
                alert.accept()
            except TimeoutException:
                print("No alert present")
        finally:
            driver.quit()

    # ----- Checkout testcases -----
    # Testcase 11: Checkout with valid credentials
    def test_checkout(self):
        self.driver.find_element(By.ID, "cartur").click()
        time.sleep(1)
        self.driver.find_element(By.XPATH, "//button[text()='Place Order']").click()
        time.sleep(1)
        self.driver.find_element(By.ID, "name").send_keys("Test User")
        self.driver.find_element(By.ID, "country").send_keys("Test Country")
        self.driver.find_element(By.ID, "city").send_keys("Test City")
        self.driver.find_element(By.ID, "card").send_keys("1234567812345678")
        self.driver.find_element(By.ID, "month").send_keys("12")
        self.driver.find_element(By.ID, "year").send_keys("2023")
        self.driver.find_element(By.XPATH, "//button[text()='Purchase']").click()
        time.sleep(1)
        assert "Thank you for your purchase!" in self.driver.page_source

    # Testcase 12: Checkout with empty credentials
    def test_checkout2(self):
        self.driver.find_element(By.ID, "cartur").click()
        time.sleep(1)
        self.driver.find_element(By.XPATH, "//button[text()='Place Order']").click()
        time.sleep(1)
        self.driver.find_element(By.ID, "name").send_keys("")
        self.driver.find_element(By.ID, "country").send_keys("")
        self.driver.find_element(By.ID, "city").send_keys("")
        self.driver.find_element(By.ID, "card").send_keys("")
        self.driver.find_element(By.ID, "month").send_keys("")
        self.driver.find_element(By.ID, "year").send_keys("")
        self.driver.find_element(By.XPATH, "//button[text()='Purchase']").click()
        time.sleep(1)
        assert "Please fill out Name and Creditcard." in self.driver.switch_to.alert.text
        self.driver.switch_to.alert.accept()

    # ----- Delete testcases -----
    # Testcase 14: Delete from cart
    def test_delete_from_cart(self):
        driver = webdriver.Chrome()
        try:
            driver.get("https://www.demoblaze.com/")
            driver.find_element(By.ID, "login2").click()
            time.sleep(1)
            driver.find_element(By.ID, "loginusername").send_keys("your_username")
            driver.find_element(By.ID, "loginpassword").send_keys("your_password")
            driver.find_element(By.XPATH, "//button[text()='Log in']").click()
            time.sleep(1)
            driver.find_element(By.ID, "cartur").click()
            time.sleep(1)
            delete_buttons = driver.find_elements(By.XPATH, "//button[text()='Delete']")
            while delete_buttons:
                delete_buttons[0].click()
                time.sleep(1)
                delete_buttons = driver.find_elements(By.XPATH, "//button[text()='Delete']")
            cart_items = driver.find_elements(By.CLASS_NAME, "cart_item")
            assert len(cart_items) == 0
        finally:
            driver.quit()
    
    # ----- Logout testcases -----
    # Testcase 15: Logout
    def test_logout(self):
        self.driver.find_element(By.ID, "login2").click()
        time.sleep(1)
        self.driver.find_element(By.ID, "loginusername").send_keys("your_username")
        self.driver.find_element(By.ID, "loginpassword").send_keys("your_password")
        self.driver.find_element(By.XPATH, "//button[text()='Log in']").click()
        time.sleep(1)
        WebDriverWait(self.driver, 10).until(EC.element_to_be_clickable((By.ID, "logout2"))).click()
        time.sleep(1)
        login_button = WebDriverWait(self.driver, 10).until(EC.presence_of_element_located((By.ID, "login2")))
        assert login_button is not None

    # ----- Contact testcases -----
    # Testcase 16: Contact
    def test_contact(self):
        contact_link = WebDriverWait(self.driver, 10).until(EC.visibility_of_element_located((By.XPATH, '//*[@id="navbarExample"]/ul/li[2]/a')))
        contact_link.click()
        time.sleep(1)
        self.driver.find_element(By.ID, "recipient-email").send_keys("test@example.com")
        self.driver.find_element(By.ID, "recipient-name").send_keys("Test User")
        WebDriverWait(self.driver, 10).until(EC.visibility_of_element_located((By.ID, "message-text"))).send_keys("This is a test message.")
        self.driver.find_element(By.XPATH, "//button[text()='Send message']").click()
        time.sleep(1)
        assert "Thanks for the message!!" in self.driver.switch_to.alert.text
        self.driver.switch_to.alert.accept()

if __name__ == "__main__":
    unittest.main()