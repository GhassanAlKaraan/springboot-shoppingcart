package com.example.dreamshops.services.cartitem;

import org.springframework.stereotype.Service;

import com.example.dreamshops.models.Cart;
import com.example.dreamshops.models.CartItem;
import com.example.dreamshops.models.Product;
import com.example.dreamshops.repositories.CartItemRepository;
import com.example.dreamshops.repositories.CartRepository;
import com.example.dreamshops.services.cart.ICartService;
import com.example.dreamshops.services.product.IProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
  private final CartItemRepository cartItemRepository;
  private final CartRepository cartRepository;

  private final ICartService iCartService;
  private final IProductService iProductService;

  @Override
  public void addItemToCart(Long cartId, Long productId, int quantity) {
    // 1 Get Cart // 2 Get Product // 3 Check if product already exists in the cart
    // 4 if yes, increase quantity by requested quantity
    // 5 if no , initiate a new CartItem entry
    Cart cart = iCartService.getCart(cartId);
    Product product = iProductService.getProductById(productId);
    CartItem cartItem = cart
        .getItems()
        .stream()
        .filter(item -> item.getProduct().getId().equals(productId))
        .findFirst()
        .orElse(new CartItem());

    if (cartItem.getId() == null) {
      cartItem.setCart(cart);
      cartItem.setProduct(product);
      cartItem.setQuantity(quantity);
      cartItem.setUnitPrice(product.getPrice());
    } else {
      cartItem.setQuantity(cartItem.getQuantity() + quantity);
    }

    cartItem.setTotalPrice();
    cart.addItem(cartItem);

    cartItemRepository.save(cartItem);
    cartRepository.save(cart);
  }

  @Override
  public void removeItemFromCart(Long cartId, Long productId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'removeItemFromCart'");
  }

  @Override
  public void updateItemQuantity(Long cartId, Long productId, int quantity) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateItemQuantity'");
  }

}
